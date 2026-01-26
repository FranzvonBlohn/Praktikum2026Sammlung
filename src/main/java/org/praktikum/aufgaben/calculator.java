package org.praktikum.aufgaben;

public class calculator {

    public static void main(String[] args) {
        System.out.println( calculate("add", 1, 2) );
        System.out.println( calculate("subtract", 5, 3) );
        // System.out.println( calculate("foo", 1, 2) ); // would throw IllegalArgumentException
    }

    public static int calculate(String operation, int num_1, int num_2) {
        return switch (operation) {
            case "add" -> num_1 + num_2;
            case "subtract" -> num_1 - num_2;
            default -> throw new IllegalArgumentException("Keine gültige Operation: " + operation);
        };
    }
}





