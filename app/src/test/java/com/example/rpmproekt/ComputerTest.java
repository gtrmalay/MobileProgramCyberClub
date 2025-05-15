package com.example.rpmproekt;

import com.example.tdsclub.models.Computer;
import org.junit.Test;

public class ComputerTest {

    @Test
    public void computerId_CanBeSetAndRetrieved() throws Exception {
        Computer computer = new Computer();

        java.lang.reflect.Field computerIdField = Computer.class.getDeclaredField("computerId");
        computerIdField.setAccessible(true);
        computerIdField.setInt(computer, 123);

        int actualComputerId = computer.getComputerId();

        if (actualComputerId != 123) {
            throw new AssertionError("computerId should be 123, but was " + actualComputerId);
        }
    }

    @Test
    public void computerNumber_CanBeSetAndRetrieved() throws Exception {

        Computer computer = new Computer();
        java.lang.reflect.Field computerNumberField = Computer.class.getDeclaredField("computerNumber");
        computerNumberField.setAccessible(true);
        computerNumberField.set(computer, "PC-001");

        String actualComputerNumber = computer.getComputerNumber();

        if (!"PC-001".equals(actualComputerNumber)) {
            throw new AssertionError("computerNumber should be 'PC-001', but was '" + actualComputerNumber + "'");
        }
    }

    @Test
    public void isFree_CanBeSetAndRetrieved() throws Exception {

        Computer computer = new Computer();
        java.lang.reflect.Field isFreeField = Computer.class.getDeclaredField("isFree");
        isFreeField.setAccessible(true);
        isFreeField.setBoolean(computer, true);


        boolean actualIsFree = computer.isFree();

        if (!actualIsFree) {
            throw new AssertionError("isFree should be true, but was " + actualIsFree);
        }
    }
}