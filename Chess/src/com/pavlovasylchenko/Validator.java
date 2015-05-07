package com.pavlovasylchenko;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Validator {

    static int width = 7;

    static int height = 7;

    public static void main(String[] args) throws IOException {
        List<String> list = Files.readAllLines(Paths.get("output.txt"));
        System.out.println("Started...");
        for (String item : list) {
            Figure[][] figures = new Figure[height][width];
            int posX = 0;
            int posY = 0;
            for (int i = 0; i < item.length(); i++) {
                if (item.toCharArray()[i] == 'Q') {
                    figures = fillConstraints(posY, posX, Figure.QUEEN, figures);
                }
                if (item.toCharArray()[i] == 'R') {
                    figures = fillConstraints(posY, posX, Figure.ROOK, figures);
                }
                if (item.toCharArray()[i] == 'K') {
                    figures = fillConstraints(posY, posX, Figure.KING, figures);
                }
                if (item.toCharArray()[i] == 'N') {
                    figures = fillConstraints(posY, posX, Figure.KNIGHT, figures);
                }
                if (item.toCharArray()[i] == 'B') {
                    figures = fillConstraints(posY, posX, Figure.BISHOP, figures);
                }
                if (figures == null) {
                    System.out.println("Error on " + item);
                }
                posX++;
                if (posX >= width) {
                    posX = 0;
                    posY++;
                }
            }
        }
        System.out.println("Finished!");
    }

    // В места которые нельзя ставить фигуры ставим NONE, фигуры ставить можно только в места null.
    private static Figure[][] fillConstraints(int y, int x, Figure figure, Figure[][] field) {
        final int val = width > height ? width : height;
        switch (figure) {
            case QUEEN: {
                if (!checkRook(y, x, field) || !checkBishop(y, x, field)) return null;
                break;
            }
            case ROOK: {
                if (!checkRook(y, x, field)) return null;
                break;
            }
            case BISHOP: {
                if (!checkBishop(y, x, field)) return null;
                break;
            }
            case KING: {
                if (!checkKing(y, x, field)) return null;
                break;
            }
            case KNIGHT: {
                if (!checkKnight(y, x, field)) return null;
                break;
            }
        }
        field[y][x] = figure;
        return field;
    }

    private static boolean checkKnight(int y, int x, Figure[][] field) {
        for (int _y = -2; _y <= 2; _y++) {
            for (int _x = -2; _x <= 2; _x++) {
                if (Math.abs(_x) != Math.abs(_y) && _x != 0 && _y != 0) {
                    if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                        if (field[_y + y][_x + x] != null && field[_y + y][_x + x] != Figure.NONE)
                            return false;
                        field[_y + y][_x + x] = Figure.NONE;
                    }
                }
            }
        }
        return true;
    }

    private static boolean checkKing(int y, int x, Figure[][] field) {
        for (int _y = -1; _y <= 1; _y++) {
            for (int _x = -1; _x <= 1; _x++) {
                if (_y == 0 && _x == 0) continue;
                if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                    if (field[_y + y][_x + x] != null && field[_y + y][_x + x] != Figure.NONE) return false;
                    field[_y + y][_x + x] = Figure.NONE;
                }
            }
        }
        return true;
    }

    private static boolean checkBishop(int y, int x, Figure[][] field) {
        final int val = width > height ? width : height;
        for (int i = 0; i < val; i++) {
            if (y + i < height && x + i < width) {
                if (field[y + i][x + i] != null && field[y + i][x + i] != Figure.NONE) return false;
                field[y + i][x + i] = Figure.NONE;
            }

            if (y - i >= 0 && x - i >= 0) {
                if (field[y - i][x - i] != null && field[y - i][x - i] != Figure.NONE) return false;
                field[y - i][x - i] = Figure.NONE;
            }

            if (y + i < height && x - i >= 0) {
                if (field[y + i][x - i] != null && field[y + i][x - i] != Figure.NONE) return false;
                field[y + i][x - i] = Figure.NONE;
            }

            if (y - i >= 0 && x + i < width) {
                if (field[y - i][x + i] != null && field[y - i][x + i] != Figure.NONE) return false;
                field[y - i][x + i] = Figure.NONE;
            }
        }
        return true;
    }

    private static boolean checkRook(int y, int x, Figure[][] field) {
        for (int i = 0; i < height; i++) {
            if (field[i][x] != null && field[i][x] != Figure.NONE) return false;
            field[i][x] = Figure.NONE;
        }
        for (int i = 0; i < width; i++) {
            if (field[y][i] != null && field[y][i] != Figure.NONE) return false;
            field[y][i] = Figure.NONE;
        }
        return true;
    }
}
