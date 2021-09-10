package com.ecfinder.test;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class ElementCollectionWithoutAnnotation {
    private String name;

    private LocalDateTime dateTime;

    private int price;


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
}
