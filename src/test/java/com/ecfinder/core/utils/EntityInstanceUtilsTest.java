package com.ecfinder.core.utils;

import com.ecfinder.test.ClassWithoutInstance;
import com.ecfinder.test.EntityTest;
import com.ecfinder.test.EntityTestWithoutAnnotation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityInstanceUtilsTest {

    @Test
    void getNewInstanceEquals() {
        EntityInstanceUtils<EntityTest> instanceUtils = new EntityInstanceUtils<>();

        EntityTest entityTest = instanceUtils.getNewInstance(EntityTest.class);
        entityTest.setName("Test");

        assertEquals(new EntityTest("Test"), entityTest);
    }

    @Test
    void getNewInstanceNotEquals() {
        EntityInstanceUtils<?> instanceUtils = new EntityInstanceUtils<>();

        Object entityTestWithoutAnnotation = instanceUtils.getNewInstance(EntityTestWithoutAnnotation.class);

        assertNotEquals(new EntityTest(), entityTestWithoutAnnotation);
    }


    @Test
    void getNewInstanceCreateInstanceException() {
        EntityInstanceUtils<ClassWithoutInstance> instanceUtils = new EntityInstanceUtils<>();

        assertThrows(Exception.class, () -> instanceUtils.getNewInstance(ClassWithoutInstance.class));
    }

}
