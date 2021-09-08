package com.ecfinder.core.utils;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class DynamicalMethodUtils {

    private final ResultSet rs;

    public DynamicalMethodUtils(ResultSet rs) {
        this.rs = rs;
    }

    public LocalDateTime getLocalDateTime(String fieldName) throws SQLException {
        return rs.getTimestamp(fieldName).toLocalDateTime();
    }

    public InputStream getInputStream(String fieldName) throws SQLException {
        InputStream inputStream = rs.getAsciiStream(fieldName);
        if (inputStream == null)
            inputStream = rs.getBinaryStream(fieldName);
        return inputStream;
    }

    public LocalDate getLocalDate(String fieldName) throws SQLException {
        return rs.getDate(fieldName).toLocalDate();
    }

}
