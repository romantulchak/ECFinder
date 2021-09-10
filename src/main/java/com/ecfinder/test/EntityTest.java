package com.ecfinder.test;

import com.ecfinder.core.anotation.ECFEntity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@ECFEntity(tablePrefix = "entity_test")
@Entity
public class EntityTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ElementCollection
    private List<ElementCollectionTest> elementCollectionTests;

    @ElementCollection
    private List<ElementCollectionWithoutAnnotation> elementCollectionWithoutAnnotations;

    public EntityTest(){}

    public EntityTest(String name){
        this.name = name;
    }

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

    public List<ElementCollectionWithoutAnnotation> getElementCollectionWithoutAnnotations() {
        return elementCollectionWithoutAnnotations;
    }

    public void setElementCollectionWithoutAnnotations(List<ElementCollectionWithoutAnnotation> elementCollectionWithoutAnnotations) {
        this.elementCollectionWithoutAnnotations = elementCollectionWithoutAnnotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityTest)) return false;
        EntityTest that = (EntityTest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
