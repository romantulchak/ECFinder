package com.ecfinder.core;

import com.ecfinder.core.anotation.ECF;
import com.ecfinder.core.anotation.ECFEntity;
import com.ecfinder.core.mapper.EntityRowMapper;
import com.ecfinder.exception.ClassNotMarkedAsElementCollectionException;
import com.ecfinder.exception.ClassNotMarkedAsParentEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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


    public List<E> handleElements(long entityId, Class<?> clazz, Class<?> parentEntity) {
        if (clazz.isAnnotationPresent(ECF.class) && clazz.isAnnotationPresent(Embeddable.class)) {
            String parentColumnIdName = getParentColumnIdName(parentEntity);
            EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
            String tableName = getTableName(clazz, parentEntity);
            return jdbcTemplate.query(String.format(SELECT_QUERY, tableName, parentColumnIdName), rowMapper, entityId);
        }
        throw new ClassNotMarkedAsElementCollectionException(clazz);
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
