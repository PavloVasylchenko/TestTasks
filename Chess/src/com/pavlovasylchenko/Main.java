package com.pavlovasylchenko;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Engine engine = new Engine(8, 8, Arrays.asList(
                new Figure(FigureType.KING),
                new Figure(FigureType.KING),
                new Figure(FigureType.QUEEN),
                new Figure(FigureType.QUEEN),
                new Figure(FigureType.BISHOP),
                new Figure(FigureType.BISHOP),
                new Figure(FigureType.KNIGHT)
        ));
        long first = System.currentTimeMillis();
        List<FigureType[][]> results = engine.getResult();
        long last = System.currentTimeMillis();
        System.out.println(((last - first)) / 1000d + " секунды");
        System.out.println("Найдено количество: " + results.size());
        for (FigureType[][] resultField : results) {
            //printField(resultField);
        }
    }

    private static void printField(FigureType[][] field) {
        System.out.println("-------------------------");
        for (FigureType[] aField : field) {
            System.out.print("|");
            for (FigureType anAField : aField) {
                if (anAField == FigureType.KING) System.out.print("♚|");
                if (anAField == FigureType.QUEEN) System.out.print("♛|");
                if (anAField == FigureType.ROOK) System.out.print("♜|");
                if (anAField == FigureType.KNIGHT) System.out.print("♞|");
                if (anAField == FigureType.BISHOP) System.out.print("♝|");
                if (anAField == FigureType.NONE) System.out.print("-|");
                if (anAField == null) System.out.print(" |");
            }
            System.out.println();
        }
        System.out.println("-------------------------");
    }
}