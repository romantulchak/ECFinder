package com.ecfinder.test;

import com.ecfinder.core.anotation.ECF;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Objects;

@ECF(tableName = "collections")
@Embeddable
public class ElementCollectionTest {

    private String name;

    private LocalDateTime dateTime;

    private int price;

    public ElementCollectionTest(){}

    public ElementCollectionTest(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementCollectionTest)) return false;
        ElementCollectionTest that = (ElementCollectionTest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
