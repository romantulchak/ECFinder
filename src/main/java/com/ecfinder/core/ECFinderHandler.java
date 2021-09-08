package com.ecfinder.core;

import com.ecfinder.core.anotation.ECF;
import com.ecfinder.core.mapper.EntityRowMapper;
import com.ecfinder.exception.ClassNotMarkedAsElementCollectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Embeddable;
import java.util.List;

//TODO: how to get table?
@Component
public class ECFinderHandler<E> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ECFinderHandler(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<E> handleElements(long entityId, String table, Class<?> clazz) {
        if (clazz.isAnnotationPresent(ECF.class) && clazz.isAnnotationPresent(Embeddable.class)) {
            EntityRowMapper<E> rowMapper = new EntityRowMapper<>(clazz);
            return jdbcTemplate.query(String.format("SELECT * FROM %s WHERE trip_id = ?", table), rowMapper, entityId);
        }
        throw new ClassNotMarkedAsElementCollectionException(clazz);
    }

}
