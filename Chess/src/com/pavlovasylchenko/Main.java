package com.pavlovasylchenko;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine(6, 6, Arrays.asList(
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.QUEEN
        ));
        long first = System.currentTimeMillis();
        Set<Figure[][]> results = engine.getResult();
        long last = System.currentTimeMillis();
        System.out.println(((last - first)) / 1000d + " секунды");
        System.out.println("Найдено количество: " + results.size());
        for (Figure[][] resultField : results) {
            //printField(resultField);
        }
    }

    public static void printField(Figure[][] field) {
        System.out.println("-------------------------");
        for (Figure[] aField : field) {
            System.out.print("|");
            for (Figure anAField : aField) {
                if (anAField == Figure.KING) System.out.print("♚|");
                if (anAField == Figure.QUEEN) System.out.print("♛|");
                if (anAField == Figure.ROOK) System.out.print("♜|");
                if (anAField == Figure.KNIGHT) System.out.print("♞|");
                if (anAField == Figure.BISHOP) System.out.print("♝|");
                if (anAField == Figure.NONE) System.out.print("-|");
                if (anAField == null) System.out.print(" |");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}
