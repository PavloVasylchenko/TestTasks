package com.pavlovasylchenko;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    /*
        Result:
        Всего вариантов: 3063828
        3.17 секунды
     */
    // javac -d out src/com/**/*.java
    // java -cp out/ com.pavlovasylchenko.Main
    public static void main(String[] args) throws IOException {

        Engine engine = new Engine(7, 7, Arrays.asList(
                Figure.QUEEN,
                Figure.QUEEN,
                Figure.BISHOP,
                Figure.BISHOP,
                Figure.KNIGHT,
                Figure.KING,
                Figure.KING
        ));

        long first = System.currentTimeMillis();
        List<Field> results = engine.getResult(false);
        long last = System.currentTimeMillis();
        System.out.println(((last - first)) / 1000d + " секунды");
        System.out.println("Найдено количество: " + results.size());
        saveToFile(results);
    }

    //sort output.txt | uniq -u > output2.txt
    private static void saveToFile(List<Field> results) throws IOException {
        BufferedWriter bw = Files.newBufferedWriter(new File("output.txt").toPath(), StandardOpenOption.CREATE);
        for (Field result : results) {
            StringBuilder sb = new StringBuilder();
            for (Figure[] aField : result.getFieldArray()) {
                for (Figure anAField : aField) {
                    if (anAField == Figure.KING) sb.append("K");
                    if (anAField == Figure.QUEEN) sb.append("Q");
                    if (anAField == Figure.ROOK) sb.append("R");
                    if (anAField == Figure.KNIGHT) sb.append("N");
                    if (anAField == Figure.BISHOP) sb.append("B");
                    if (anAField == Figure.NONE) sb.append("-");
                    if (anAField == null) sb.append("-");
                }
            }
            bw.write(sb.toString());
            bw.write("\r\n");
        }
        bw.close();
    }

//    public static void main(String[] args) {
//
//        ReadInput readInput = new ReadInput().invoke();
//
//        int width = readInput.getWidth();
//        int height = readInput.getHeight();
//        List<Figure> figures = readInput.getFigures();
//
//        Engine engine = new Engine(width, height, figures);
//        long first = System.currentTimeMillis();
//        List<Field> results = engine.getResult(true);
//        long last = System.currentTimeMillis();
//        System.out.println(((last - first)) / 1000d + " секунды");
//        //System.out.println("Найдено количество: " + results.size());
//        //for (Field resultField : results) {
//            //printField(resultField.getFieldArray());
//        //}
//    }

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

    private static class ReadInput {
        private int width;
        private int height;
        private List<Figure> figures;

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public List<Figure> getFigures() {
            return figures;
        }

        public ReadInput invoke() {
            Scanner s = new Scanner(System.in);
            System.out.println("Введите ширину поля: ");
            width = s.nextInt();
            System.out.println("Введите высоту поля: ");
            height = s.nextInt();

            System.out.println("Введите количество ферзей: ");
            int queenCount = s.nextInt();

            System.out.println("Введите количество ладей: ");
            int rookCount = s.nextInt();

            System.out.println("Введите количество офицеров: ");
            int bishopCount = s.nextInt();

            System.out.println("Введите количество коней: ");
            int knightCount = s.nextInt();

            System.out.println("Введите количество королей: ");
            int kingCount = s.nextInt();

            figures = new ArrayList<>();

            for (int i = 0; i < queenCount; i++) {
                figures.add(Figure.QUEEN);
            }
            for (int i = 0; i < rookCount; i++) {
                figures.add(Figure.ROOK);
            }
            for (int i = 0; i < bishopCount; i++) {
                figures.add(Figure.BISHOP);
            }
            for (int i = 0; i < knightCount; i++) {
                figures.add(Figure.KNIGHT);
            }
            for (int i = 0; i < kingCount; i++) {
                figures.add(Figure.KING);
            }
            return this;
        }
    }
}
