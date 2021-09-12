package com.ecfinder.core;

import com.ecfinder.core.anotation.ECF;
import com.ecfinder.core.anotation.ECFEntity;
import com.ecfinder.core.mapper.EntityRowMapper;
import com.ecfinder.exception.ClassNotMarkedAsElementCollectionException;
import com.ecfinder.exception.ClassNotMarkedAsParentEntityException;
import com.ecfinder.exception.TableNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ECFinderHandler<E> {

    private static final String SELECT_QUERY = "SELECT * FROM %s WHERE %s = ?";
    private static final String SEPARATOR = "_";
    private static final String ID_PREFIX = SEPARATOR + "id";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ECFinderHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * Check if Embeddable class and Parent Entity marked with the necessary annotations
     * gets parent column id name and table name and it makes a query to the database
     *
     * @param entityId Depending on it the search will take place
     * @param clazz Embeddable class
     * @param parentEntity parent entity class
     * @return List of elements depending on Generic type
     */
    public List<E> handleElements(long entityId, Class<?> clazz, Class<?> parentEntity) {
        if (clazz.isAnnotationPresent(ECF.class) && clazz.isAnnotationPresent(Embeddable.class)) {
            String parentColumnIdName = getParentColumnIdName(parentEntity);
            EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
            String tableName = getTableName(clazz, parentEntity);
            if (!isTableExists(tableName)){
                throw new TableNotExistsException(tableName);
            }
            return jdbcTemplate.query(String.format(SELECT_QUERY, tableName, parentColumnIdName), rowMapper, entityId);
        }
        throw new ClassNotMarkedAsElementCollectionException(clazz);
    }

    private boolean isTableExists(String tableName){
        DataSource dataSource = jdbcTemplate.getDataSource();
        if (dataSource != null){
            try(Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, tableName.toUpperCase(Locale.ROOT), null);
                return tables.next();
            }catch (SQLException e){
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
            if (!ecfEntity.tableId().isEmpty()){
                return ecfEntity.tableId();
            }
            String[] classWords = parentEntity.getSimpleName().split("(?=[A-Z])");
            if (classWords.length > 1) {
                String columnName = Arrays.stream(classWords).limit(classWords.length - 1L)
                        .map(s -> s.toLowerCase(Locale.ROOT) + SEPARATOR)
                        .collect(Collectors.joining());
                columnName += classWords[classWords.length - 1].toLowerCase(Locale.ROOT) + ID_PREFIX;
                return columnName;
            }
            return classWords[0].toLowerCase(Locale.ROOT) + ID_PREFIX;
        }
        throw new ClassNotMarkedAsParentEntityException(parentEntity);
    }

}
