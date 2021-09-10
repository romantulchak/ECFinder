package com.ecfinder.test;

import javax.persistence.*;
import java.util.List;

@Entity
public class EntityTestWithoutAnnotation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ElementCollection
    private List<ElementCollectionTest> elementCollectionTests;

    @ElementCollection
    private List<ElementCollectionWithoutAnnotation> elementCollectionWithoutAnnotations;


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
}
