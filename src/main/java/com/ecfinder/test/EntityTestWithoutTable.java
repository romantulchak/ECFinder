package com.ecfinder.test;

import com.ecfinder.core.anotation.ECFEntity;

import javax.persistence.*;
import java.util.List;

@ECFEntity(tablePrefix = "entity_test_without_table")
@Entity
public class EntityTestWithoutTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ElementCollection
    private List<ElementCollectionTest> elementCollectionTests;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ElementCollectionTest> getElementCollectionTests() {
        return elementCollectionTests;
    }

    public void setElementCollectionTests(List<ElementCollectionTest> elementCollectionTests) {
        this.elementCollectionTests = elementCollectionTests;
    }
}
