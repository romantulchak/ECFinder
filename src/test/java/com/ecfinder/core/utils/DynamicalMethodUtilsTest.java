package com.ecfinder.core.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class DynamicalMethodUtilsTest {

    @Mock
    ResultSet resultSet;

    @InjectMocks
    DynamicalMethodUtils dynamicalMethodUtils;

    private final LocalDateTime localDateTime = LocalDateTime.of(2021, 9, 10, 18, 40, 9);

    @BeforeEach
    public void setResultSet() throws SQLException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getLocalDateTime_equalsAndNotNull() throws SQLException {
        Mockito.when(resultSet.getTimestamp("datetime")).thenReturn(Timestamp.valueOf(localDateTime));

        LocalDateTime datetime = dynamicalMethodUtils.getLocalDateTime("datetime");

        assertNotNull(datetime);
        assertEquals(localDateTime, datetime);
    }

    @Test
    void getLocalDateTime_notEqualsAndNotNUll() throws SQLException {
        Mockito.when(resultSet.getTimestamp("datetime")).thenReturn(Timestamp.valueOf(localDateTime));

        LocalDateTime datetime = dynamicalMethodUtils.getLocalDateTime("datetime");

        assertNotNull(datetime);
        assertNotEquals(localDateTime.plusDays(10), datetime);
    }

    @Test
    void getLocalDateTime_throwNullPointerException() {
        assertThrows(NullPointerException.class, () -> dynamicalMethodUtils.getLocalDateTime("datetime"));
    }

    @Test
    void getLocalDateTime_throwSqlException() throws SQLException {
        Mockito.when(resultSet.getTimestamp("datetime")).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> dynamicalMethodUtils.getLocalDateTime("datetime"));
    }

    @Test
    void getInputStream_equalNotNull() throws SQLException {
        String initialString = "test";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        Mockito.when(resultSet.getAsciiStream("asciiStream")).thenReturn(targetStream);

        InputStream asciiStream = dynamicalMethodUtils.getInputStream("asciiStream");

        assertNotNull(asciiStream);
        assertEquals(targetStream, asciiStream);
    }

    @Test
    void getInputStream_notEqualNotNull() throws SQLException {
        String initialString = "test";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());
        Mockito.when(resultSet.getAsciiStream("asciiStream")).thenReturn(targetStream);
        targetStream = new ByteArrayInputStream("".getBytes());

        InputStream asciiStream = dynamicalMethodUtils.getInputStream("asciiStream");

        assertNotNull(asciiStream);
        assertNotEquals(targetStream, asciiStream);
    }

    @Test
    void getInputStream_isNull() throws SQLException {
        Mockito.when(resultSet.getAsciiStream("asciiStream")).thenReturn(null);

        InputStream asciiStream = dynamicalMethodUtils.getInputStream("asciiStream");

        assertNull(asciiStream);
    }

    @Test
    void getInputStream_throwSqlException() throws SQLException {
        Mockito.when(resultSet.getAsciiStream("asciiStream")).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> dynamicalMethodUtils.getInputStream("asciiStream"));
    }


    @Test
    void getLocalDate_equalNotNull() throws SQLException {
        Mockito.when(resultSet.getDate("date")).thenReturn(Date.valueOf(localDateTime.toLocalDate()));

        LocalDate date = dynamicalMethodUtils.getLocalDate("date");

        assertNotNull(date);
        assertEquals(localDateTime.toLocalDate(), date);
    }

    @Test
    void getLocalDate_notEqualNotNull() throws SQLException {
        Mockito.when(resultSet.getDate("date")).thenReturn(Date.valueOf(localDateTime.toLocalDate()));

        LocalDate date = dynamicalMethodUtils.getLocalDate("date");

        assertNotNull(date);
        assertNotEquals(localDateTime.toLocalDate().plusDays(10), date);
    }

    @Test
    void getLocalDate_isNull() throws SQLException {
        Mockito.when(resultSet.getDate("date")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> dynamicalMethodUtils.getLocalDate("date"));
    }

    @Test
    void getLocalDate_throwSqlException() throws SQLException {
        Mockito.when(resultSet.getDate("date")).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> dynamicalMethodUtils.getLocalDate("date"));
    }

    @Test
    void getInteger_equalThree() throws SQLException {
        Mockito.when(resultSet.getInt("int")).thenReturn(3);

        int integer = dynamicalMethodUtils.getInteger("int");

        assertEquals(3, integer);
    }    @Test

    void getInteger_notEqualThree() throws SQLException {
        Mockito.when(resultSet.getInt("int")).thenReturn(5);

        int integer = dynamicalMethodUtils.getInteger("int");

        assertNotEquals(3, integer);
    }

    @Test
    void getInteger_throwSqlException() throws SQLException {
        Mockito.when(resultSet.getInt("int")).thenThrow(new SQLException());

        assertThrows(SQLException.class, () -> dynamicalMethodUtils.getInteger("int"));
    }
}
