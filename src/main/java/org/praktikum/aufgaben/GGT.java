package org.praktikum.aufgaben;

import java.util.List;

public class GGT {

    public static void main(String[] args) {

        List<Integer> numList = List.of(1000, 1200, 10000);

        long start = System.nanoTime();
        System.out.println(getBiggestDivider(numList));
        long end = System.nanoTime();

        double elapsedTime = (end - start) / 1_000_000.0;

        System.out.println("Finished in: " + elapsedTime + " milli seconds");

    }

    public static boolean checkDivisible(int num, int divider){
        return (num % divider == 0);
    }

    public static Integer getBiggestDivider(List<Integer> list) {
        int biggestPossibleDivider = Math.round(getSmallest(list))/2;
        int biggestDivider = 1;


        for (int i = 1; i <= biggestPossibleDivider ; i++) {
            boolean allDivisible = true;

            for (int num : list) {

                if ( !(checkDivisible(num, i)) ) {
                    allDivisible = false;
                    break;
                }

            }

            if ((allDivisible) && (i > biggestDivider)) {
                biggestDivider = i;
            }

        }


        return biggestDivider;
    }

    public static Integer getSmallest(List<Integer> list) {
        int smallest = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) < smallest) {
                smallest = list.get(i);
            }
        }

        return smallest;
    }

}
