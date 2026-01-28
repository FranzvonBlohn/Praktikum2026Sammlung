package org.praktikum.aufgaben;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Sort {
    public static void main(String[] args) {
        testSort("basic", 10, "random", 1000);
    }

    private static List<Integer> shuffle(List<Integer> list) {
        Collections.shuffle(list);
        return list;
    }


    private static List<Integer> move(List<Integer> list, int from, int to) {
        if (from == to) return list;

        int size = list.size();
        if (from < 0 || from >= size || to < 0 || to >= size) {
            throw new IndexOutOfBoundsException("from or to index out of range");
        }

        if (from < to) {
            // rotate left the sublist [from .. to] by 1: element at 'from' goes to 'to'
            Collections.rotate(list.subList(from, to + 1), -1);
        } else {
            // from > to: rotate right the sublist [to .. from] by 1
            Collections.rotate(list.subList(to, from + 1), 1);
        }
        return list;
    }


    private static List<Integer> swap(List<Integer> list, int index1, int index2) {
        if (index1 == index2) return list;
        Collections.swap(list, index1, index2);
        return list;
    }

    private static boolean checkSorted(List<Integer> list) {
        if (list == null || list.size() <= 1) return true;

        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }
        return true;
    }


    private static List<Integer> sortBasic(List<Integer> list) {
        if (list == null || list.size() <= 1) return list;

        while (!(checkSorted(list))) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (!(list.get(i) <= list.get(i + 1))) {
                    move(list, i, i + 1);
                }
            }
        }

        return list;
    }

    private static List<Integer> sortBubble(List<Integer> list) {
        if (list == null || list.size() <= 1) return list;

        while (!(checkSorted(list))) {
            for (int i = 0; i < list.size() - 1; i++) {
                if (!(list.get(i) <= list.get(i + 1))) {
                    swap(list, i, i + 1);
                }
            }
        }

        return list;
    }

    private static List<Integer> sortInsertion(List<Integer> list) {
        if (list == null || list.size() <= 1) return list;

        for (int i = 1; i < list.size(); i++) {

            for (int c = 0; c < i; c++) {

                if (list.get(i) < list.get(c)) {
                    move(list, i, c);
                }

            }

        }


        return list;
    }

    private static List<Integer> sortSelection(List<Integer> list) {
        if (list == null || list.size() <= 1) return list;

        for (int i = 0; i < list.size() - 1; i++) {
            int smallestElement = i;
            // start at i+1 and go up to last index (inclusive)
            for (int c = i + 1; c < list.size(); c++) {

                if (list.get(c) < list.get(smallestElement)) {
                    smallestElement = c;
                }

            }
            swap(list, i, smallestElement);
        }

        return list;
    }


    public static void testSort(String sorter, int iterations, String listType, int length) {
        final int warmup = Math.max(5, iterations / 5);

        // Warmup (not measured)
        for (int i = 0; i < warmup; i++) {
            List<Integer> list = makeList(listType, length);
            runSort(sorter, list);
        }

        // Measured runs
        double sumMs = 0.0;
        for (int r = 0; r < iterations; r++) {
            List<Integer> list = makeList(listType, length);

            //System.out.println(list);

            long start = System.nanoTime();
            runSort(sorter, list);
            long end = System.nanoTime();

            double elapsedMs = (end - start) / 1_000_000.0;
            sumMs += elapsedMs;
            // (optional) record each elapsed in an array for later statistics
        }

        System.out.println("Sorter: " + sorter);
        System.out.println("List Type: " + listType);
        System.out.println("List Length: " + length);
        System.out.println("Iterations: " + iterations);
        System.out.println("Average Time: " + (sumMs / iterations) + " ms");
    }

    private static void runSort(String sorter, List<Integer> list) {
        switch (sorter) {
            case "basic" -> sortBasic(list);
            case "bubble" -> sortBubble(list);
            case "selection" -> sortSelection(list);
            case "insertion" -> sortInsertion(list);
            default -> throw new IllegalArgumentException("Keine gültiger Sorter: " + sorter);
        }
    }

    private static List<Integer> makeList(String listType, int length) {
        List<Integer> list = new ArrayList<>(length);
        switch (listType) {
            case "random" -> {
                var rnd = ThreadLocalRandom.current();
                for (int i = 0; i < length; i++) list.add(rnd.nextInt(length + 1));
            }
            case "shuffle" -> {
                for (int i = 0; i < length; i++) list.add(i);
                Collections.shuffle(list);
            }
            case "reverse" -> {
                for (int i = 0; i < length; i++) list.add(length - i);
            }
            default -> throw new IllegalArgumentException("Ungültiger Listen typ: " + listType);
        }
        return list;
    }
}




