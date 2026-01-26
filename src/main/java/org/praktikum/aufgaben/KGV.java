package org.praktikum.aufgaben;

import java.util.List;

public class KGV {

        public static void main(String[] args) {

            List<Integer> numList = List.of(30, 100, 10);

            long start = System.nanoTime();
            System.out.println(getSmallestMultiple(numList));
            long end = System.nanoTime();

            double elapsedTime = (end - start) / 1_000_000.0;

            System.out.println("Finished in: " + elapsedTime + " milli seconds");

        }

        public static boolean checkMultiple(int num, int multiple){
            return (multiple % num == 0);
        }

        public static Integer getSmallestMultiple(List<Integer> list) {
            int smallestPossibleMultiple = getBiggest(list);
            int smallestMultiple = 0;
            int multiplier = 1;
            boolean foundMultiple = false;

            while (!(foundMultiple)) {

                int possibleMultiple = multiplier * smallestPossibleMultiple;
                boolean allMultiple = true;

                for (int num : list) {
                    if (!(checkMultiple(num, possibleMultiple))) {
                        allMultiple = false;
                        break;
                    }
                }

                multiplier += 1;

                if (allMultiple) {
                    foundMultiple = true;
                    smallestMultiple = possibleMultiple;
                }


            }




            return smallestMultiple;
        }

        public static Integer getBiggest(List<Integer> list) {
            int biggest = list.get(0);

            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) > biggest) {
                    biggest = list.get(i);
                }
            }

            return biggest;
        }


}
