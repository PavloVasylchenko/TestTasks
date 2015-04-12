package com.pavlovasylchenko;

import java.util.Arrays;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine(7, 7, Arrays.asList(
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.KING,
                Figure.KING,
                Figure.BISHOP,
                Figure.BISHOP,
                Figure.KNIGHT
        ));
        long first = System.currentTimeMillis();
        Set<Field> results = engine.getResult();
        long last = System.currentTimeMillis();
        System.out.println(((last - first)) / 1000d + " секунды");
        System.out.println("Найдено количество: " + results.size());
        for (Field resultField : results) {
            //printField(resultField.getFieldArray());
        }
    }

    public static void printField(Figure[][] field) {
        System.out.println("-------------------------");
        //int num = 0;
        for (Figure[] aField : field) {
            System.out.print("|");
            for (Figure anAField : aField) {
                //num++;
                if (anAField == Figure.KING) System.out.print("♚|");
                if (anAField == Figure.QUEEN) System.out.print("♛|");
                if (anAField == Figure.ROOK) System.out.print("♜|");
                if (anAField == Figure.KNIGHT) System.out.print("♞|");
                if (anAField == Figure.BISHOP) System.out.print("♝|");
                if (anAField == Figure.NONE) {
                    System.out.print("-|");
                    //num--;
                }
                if (anAField == null) {
                    System.out.print(" |");
                    //num--;
                }
            }
            System.out.println();
        }
        //System.out.println(num);
        System.out.println("-------------------------");
    }
}
