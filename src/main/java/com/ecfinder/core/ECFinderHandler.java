package com.ecfinder.core;

import com.ecfinder.core.anotation.ECF;
import com.ecfinder.core.anotation.ECFEntity;
import com.ecfinder.core.anotation.ECFUnique;
import com.ecfinder.core.mapper.EntityRowMapper;
import com.ecfinder.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ECFinderHandler<E> {

    private static final String SELECT_QUERY = "SELECT * FROM %s WHERE %s = ?";
    private static final String SELECT_UNIQUE_QUERY = "SELECT * FROM %s WHERE %s = ? AND %s = ?";
    private static final String SELECT_PAGE_QUERY = "SELECT * FROM %s WHERE %s = ? LIMIT ? OFFSET ?";
    private static final String SELECT_TOTAL_ELEMENTS_COUNT_QUERY = "SELECT count(1) FROM %s WHERE %s = ?";
    private static final String SEPARATOR = "_";
    private static final String ID_PREFIX = SEPARATOR + "id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ECFinderHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<E> handleElements(long entityId, Class<?> clazz, Class<?> parentEntity) {
        checkAnnotation(clazz);
        String parentColumnIdName = getParentColumnIdName(parentEntity);
        String tableName = getTableName(clazz, parentEntity);
        EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
        if (isTableNotExists(tableName)) {
            throw new TableNotExistsException(tableName);
        }
        return jdbcTemplate.query(String.format(SELECT_QUERY, tableName, parentColumnIdName), rowMapper, entityId);
    }

    public Page<E> handleElements(long entityId, Class<?> clazz, Class<?> parentEntity, Pageable pageable) {
        checkAnnotation(clazz);
        String tableName = getTableName(clazz, parentEntity);
        String parentColumnIdName = getParentColumnIdName(parentEntity);
        int totalElements = getTotalElements(entityId, tableName, parentColumnIdName);
        EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
        if (isTableNotExists(tableName)) {
            throw new TableNotExistsException(tableName);
        }
        List<E> query = jdbcTemplate.query(String.format(SELECT_PAGE_QUERY, tableName, parentColumnIdName), rowMapper, entityId, pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(query, pageable, totalElements);

    }

    public <U> E handleElements(long entityId, U uniqueValue, Class<?> clazz, Class<?> parentEntity) {
        checkAnnotation(clazz);
        String parentColumnIdName = getParentColumnIdName(parentEntity);
        String tableName = getTableName(clazz, parentEntity);
        String uniqueColumnName = getUniqueColumnName(clazz);
        EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
        if (isTableNotExists(tableName)) {
            throw new TableNotExistsException(tableName);
        }
        return jdbcTemplate.queryForObject(String.format(SELECT_UNIQUE_QUERY, tableName, parentColumnIdName, uniqueColumnName), rowMapper, entityId, uniqueValue);
    }

    private String getUniqueColumnName(Class<?> clazz) {
        Field field = getUniqueField(clazz);
        ECFUnique annotation = field.getAnnotation(ECFUnique.class);
        if (!annotation.columnName().isEmpty()) {
            return annotation.columnName();
        }
        return getColumnName(field.getName());
    }

    private Field getUniqueField(Class<?> clazz) {
        List<Field> fields = Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ECFUnique.class))
                .collect(Collectors.toList());
        if (fields.isEmpty()) {
            throw new FieldsNotMarkedAsUniqueException(clazz);
        }
        if (fields.size() >= 2) {
            String fieldsName = fields.stream().map(String::valueOf)
                    .collect(Collectors.joining(",", "{", "}"));
            throw new MoreFieldsThanOneMarkedAsUnique(fieldsName);
        }
        return fields.get(0);
    }

    private void checkAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ECF.class) || !clazz.isAnnotationPresent(Embeddable.class)) {
            throw new ClassNotMarkedAsElementCollectionException(clazz);
        }
    }

    private int getTotalElements(long entityId, String tableName, String parentColumnIdName) {
        try {
            return Objects.requireNonNull(jdbcTemplate.queryForObject(String.format(SELECT_TOTAL_ELEMENTS_COUNT_QUERY, tableName, parentColumnIdName),
                    (rs, rowNum) -> rs.getInt(1), entityId));
        } catch (NullPointerException e) {
            throw new TotalElementsCountNullException(entityId);
        }
    }

    private boolean isTableNotExists(String tableName) {
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource != null) {
            try (Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet upperTableName = metaData.getTables(null, null, tableName.toUpperCase(Locale.ROOT), null);
                ResultSet lowerTableName = metaData.getTables(null, null, tableName, null);
                return !upperTableName.next() && !lowerTableName.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new NullPointerException(String.format("Datasource for jdbc %s is null", jdbcTemplate));
    }

    private String getTableName(Class<?> clazz, Class<?> parentEntity) {
        ECFEntity annotation = parentEntity.getAnnotation(ECFEntity.class);
        if (annotation != null) {
            ECF ecf = clazz.getDeclaredAnnotation(ECF.class);
            return annotation.tablePrefix() + SEPARATOR + ecf.tableName();
        }
        throw new ClassNotMarkedAsParentEntityException(parentEntity);
    }

    private String getParentColumnIdName(Class<?> parentEntity) {
        ECFEntity ecfEntity = parentEntity.getDeclaredAnnotation(ECFEntity.class);
        if (ecfEntity != null) {
            if (!ecfEntity.tableId().isEmpty()) {
                return ecfEntity.tableId();
            }
            return getColumnName(parentEntity.getSimpleName()) + ID_PREFIX;
        }
        throw new ClassNotMarkedAsParentEntityException(parentEntity);
    }

    private String getColumnName(String name) {
        String[] classWords = name.split("(?=[A-Z])");
        if (classWords.length > 1) {
            String columnName = Arrays.stream(classWords).limit(classWords.length - 1L)
                    .map(s -> s.toLowerCase(Locale.ROOT) + SEPARATOR)
                    .collect(Collectors.joining());
            columnName += classWords[classWords.length - 1].toLowerCase(Locale.ROOT);
            return columnName.toLowerCase(Locale.ROOT);
        }
        return classWords[0].toLowerCase(Locale.ROOT);
    }

}
