package com.ecfinder.core;

import com.ecfinder.exception.ClassNotMarkedAsElementCollectionException;
import com.ecfinder.exception.ClassNotMarkedAsParentEntityException;
import com.ecfinder.test.ElementCollectionTest;
import com.ecfinder.test.ElementCollectionWithoutAnnotation;
import com.ecfinder.test.EntityTest;
import com.ecfinder.test.EntityTestWithoutAnnotation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
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


}
