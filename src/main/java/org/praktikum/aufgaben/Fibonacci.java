package org.praktikum.aufgaben;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fibonacci {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int amount = scanner.nextInt();

        List<Integer> list = new ArrayList<>(List.of(1, 1));

        for (int i = 0; i < amount ; i++) {
            list.add(list.get(list.size() - 1) + list.get(list.size() - 2));
        }

        System.out.println(list);

    }
}
