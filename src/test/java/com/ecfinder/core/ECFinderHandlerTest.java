package com.ecfinder.core;

import com.ecfinder.exception.ClassNotMarkedAsElementCollectionException;
import com.ecfinder.exception.ClassNotMarkedAsParentEntityException;
import com.ecfinder.exception.TableNotExistsException;
import com.ecfinder.test.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ECFinderHandlerTest {

    ECFinderHandler<ElementCollectionTest> ecRealData;

    @Mock
    ECFinderHandler<ElementCollectionTest> mockData;

    @BeforeEach
    public void setMockData() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:test-scripts/create_entity_test.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        ecRealData = new ECFinderHandler<>(jdbcTemplate);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleElements() {
        List<ElementCollectionTest> elementCollectionTests = new ArrayList<>();
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col1"));
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col2"));
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col3"));
        Mockito.when(mockData.handleElements(1, ElementCollectionTest.class, EntityTest.class)).thenReturn(elementCollectionTests);

        List<ElementCollectionTest> mockData = this.mockData.handleElements(1, ElementCollectionTest.class, EntityTest.class);
        List<ElementCollectionTest> realData = ecRealData.handleElements(1, ElementCollectionTest.class, EntityTest.class);

        assertNotNull(realData);
        assertEquals(mockData.size(), realData.size());
        assertEquals(realData.get(0), mockData.get(0));
    }

    @Test
    void handleElements_page() {
        List<ElementCollectionTest> elementCollectionTests = new ArrayList<>();
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col1"));
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col2"));
        elementCollectionTests.add(new ElementCollectionTest("Test_elm-col3"));

        Pageable pageable = PageRequest.of(0, 1);
        PageImpl<ElementCollectionTest> page = new PageImpl<>(elementCollectionTests, pageable, 3);
        Mockito.when(mockData.handleElements(1, ElementCollectionTest.class, EntityTest.class, pageable)).thenReturn(page);
        Page<ElementCollectionTest> mockData = this.mockData.handleElements(1, ElementCollectionTest.class, EntityTest.class, pageable);
        Page<ElementCollectionTest> realData = ecRealData.handleElements(1, ElementCollectionTest.class, EntityTest.class, pageable);

        assertNotNull(realData);
        assertEquals(mockData.getTotalElements(), realData.getTotalElements());
        assertEquals(mockData.getContent().get(0), realData.getContent().get(0));
    }

    @Test
    void handleElements_withoutECFAnnotation() {
        assertThrows(ClassNotMarkedAsElementCollectionException.class, () -> {
            ecRealData.handleElements(1, ElementCollectionWithoutAnnotation.class, EntityTest.class);
        });
    }

    @Test
    void handleElements_withoutECFEntityAnnotation() {
        assertThrows(ClassNotMarkedAsParentEntityException.class, () -> {
            ecRealData.handleElements(1, ElementCollectionTest.class, EntityTestWithoutAnnotation.class);
        });
    }

    @Test
    void handleElements_throwTableNotExistsException() {
        assertThrows(TableNotExistsException.class, () -> {
            ecRealData.handleElements(1, ElementCollectionTest.class, EntityTestWithoutTable.class);
        });
    }


}
