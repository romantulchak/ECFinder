package com.ecfinder.core.mapper;


import com.ecfinder.core.utils.DynamicalMethodUtils;
import com.ecfinder.core.utils.EntityInstanceUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

//TODO: add logger, work with exception, check if table exists
public class EntityRowMapper<E> implements RowMapper<E> {
    private final String METHOD_PREFIX = "get";
    private final Class<?> elementCollectionClass;

    public EntityRowMapper(Class<?> elementCollectionClass) {
        this.elementCollectionClass = elementCollectionClass;
    }

    /**
     * Fills in the fields of Embeddable class with the corresponding data from the database
     *
     * @param rs data from database
     * @param rowNum number of row in database
     * @return Embeddable object with filled fields
     */
    @Override
    public E mapRow(ResultSet rs, int rowNum) {
        EntityInstanceUtils<E> eEntityInstanceUtils = new EntityInstanceUtils<>();
        E newInstance = eEntityInstanceUtils.getNewInstance(elementCollectionClass);
        Field[] declaredFields = newInstance.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            fillField(rs, newInstance, declaredField);
        }
        return newInstance;
    }

    private void fillField(ResultSet rs, E newInstance, Field declaredField) {
        String fieldName = getFieldName(declaredField);
        try {
            Object fieldValueByType = getFieldValueByType(declaredField.getType(), rs, fieldName);
            declaredField.setAccessible(true);
            declaredField.set(newInstance, fieldValueByType);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private String getFieldName(Field declaredField) {
        String fieldName;
        if (declaredField.isAnnotationPresent(Column.class)) {
            Column declaredAnnotation = declaredField.getDeclaredAnnotation(Column.class);
            fieldName = declaredAnnotation.name();
        } else {
            fieldName = declaredField.getName();
        }
        return fieldName;
    }

    private Object getFieldValueByType(Class<?> type, ResultSet resultSet, String fieldName) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        String methodName = getMethodName(type);
        Object result;
        try {
            Method declaredMethod = ResultSet.class.getDeclaredMethod(methodName, String.class);
            result = declaredMethod.invoke(resultSet, fieldName);
        } catch (NoSuchMethodException e) {
            result = invokeCustomDynamicalMethod(resultSet, fieldName, methodName);
        }
        return result;
    }

    private String getMethodName(Class<?> type) {
        if (type.isArray()) {
            String name = type.getTypeName().replace("[]", "s");
            return METHOD_PREFIX + StringUtils.capitalize(name);
        }
        return METHOD_PREFIX + StringUtils.capitalize(type.getSimpleName());
    }

    private Object invokeCustomDynamicalMethod(ResultSet resultSet, String fieldName, String methodName) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Object result;
        DynamicalMethodUtils dynamicalMethodUtils = new DynamicalMethodUtils(resultSet);
        Method declaredMethod = DynamicalMethodUtils.class.getDeclaredMethod(methodName, String.class);
        result = declaredMethod.invoke(dynamicalMethodUtils, fieldName);
        return result;
    }
}
