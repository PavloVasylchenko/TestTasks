package com.pavlovasylchenko;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Engine {

    int width;

    int height;

    List<Figure> figures;
    //int iter = 0;
    int wins = 0;
    Set<Field> results = new HashSet<>();

    public Engine(int width, int height, List<Figure> figures) {
        this.width = width;
        this.height = height;
        this.figures = figures;
        //results = java.util.Collections.synchronizedSet(results);
    }

    public Set<Field> getResult() {
        Figure[][] field = new Figure[height][width];
        Vector<Figure> f = new Vector<>(figures.size());
        f.addAll(figures);
        process(0, 0, f, field);
        System.out.println("Всего вариантов с повторениями " + wins);
        return results;
    }

    private void process(int y, //Позиция y
                         int x, //Позиция x
                         List<Figure> figuresLeft, //Фигуры в очереди
                         Figure[][] nextField //Частично заполненное поле с заполненными NONE
    ) {
        //System.out.println("[" + y + ":" + x + "]");
        //printField(nextField);
        //iter++;
        Figure figure = figuresLeft.remove(0);
        if (nextField[y][x] == null) {
            //Можно ставить
            Figure[][] result = fillConstraints(y, x, figure, arrCopy(nextField));
            if (result != null) {
                Vector<Figure> list = new Vector<>();
                list.addAll(figuresLeft);
                if (figuresLeft.size() == 0) {
                    //System.out.println(iter);
                    //Main.printField(result);
                    results.add(new Field(result));
                    wins++;
                } else {
                    // Попробовать создать отдельный сет который будет содержать комбинации и проверять если такая комбинация была то не трогать
                    process(0, 0, list, result);
                }
            }
        }
        figuresLeft.add(0, figure);
        Vector<Figure> list = new Vector<>();
        list.addAll(figuresLeft);
        if (x < width - 1) {
            process(y, x + 1, list, nextField);
        } else if (y < height - 1) {
            process(y + 1, 0, list, nextField);
        }
    }

    private Figure[][] fillConstraints(int y, int x, Figure figure, Figure[][] fieldClone) {
        int val = height;
        if (width > height) val = width;
        if (figure == Figure.QUEEN) {
            for (int i = 0; i < val; i++) {
                if (i < height) {
                    if (fieldClone[i][x] != null && fieldClone[i][x] != Figure.NONE) return null;
                    fieldClone[i][x] = Figure.NONE;
                }

                if (i < width) {
                    if (fieldClone[y][i] != null && fieldClone[y][i] != Figure.NONE) return null;
                    fieldClone[y][i] = Figure.NONE;
                }

                if (y + i < height && x + i < width) {
                    if (fieldClone[y + i][x + i] != null && fieldClone[y + i][x + i] != Figure.NONE) return null;
                    fieldClone[y + i][x + i] = Figure.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (fieldClone[y - i][x - i] != null && fieldClone[y - i][x - i] != Figure.NONE) return null;
                    fieldClone[y - i][x - i] = Figure.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (fieldClone[y + i][x - i] != null && fieldClone[y + i][x - i] != Figure.NONE) return null;
                    fieldClone[y + i][x - i] = Figure.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (fieldClone[y - i][x + i] != null && fieldClone[y - i][x + i] != Figure.NONE) return null;
                    fieldClone[y - i][x + i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.ROOK) {
            for (int i = 0; i < val; i++) {
                if (i < height) {
                    if (fieldClone[i][x] != null && fieldClone[i][x] != Figure.NONE) return null;
                    fieldClone[i][x] = Figure.NONE;
                }

                if (i < width) {
                    if (fieldClone[y][i] != null && fieldClone[y][i] != Figure.NONE) return null;
                    fieldClone[y][i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.BISHOP) {
            for (int i = 0; i < val; i++) {
                if (y + i < height && x + i < width) {
                    if (fieldClone[y + i][x + i] != null && fieldClone[y + i][x + i] != Figure.NONE) return null;
                    fieldClone[y + i][x + i] = Figure.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (fieldClone[y - i][x - i] != null && fieldClone[y - i][x - i] != Figure.NONE) return null;
                    fieldClone[y - i][x - i] = Figure.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (fieldClone[y + i][x - i] != null && fieldClone[y + i][x - i] != Figure.NONE) return null;
                    fieldClone[y + i][x - i] = Figure.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (fieldClone[y - i][x + i] != null && fieldClone[y - i][x + i] != Figure.NONE) return null;
                    fieldClone[y - i][x + i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.KING) {
            for (int _y = -1; _y <= 1; _y++) {
                for (int _x = -1; _x <= 1; _x++) {
                    if (_y == 0 && _x == 0) continue;
                    if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                        if (fieldClone[_y + y][_x + x] != null && fieldClone[_y + y][_x + x] != Figure.NONE) return null;
                        fieldClone[_y + y][_x + x] = Figure.NONE;
                    }
                }
            }
        }
        if (figure == Figure.KNIGHT) {
            for (int _y = -2; _y <= 2; _y++) {
                for (int _x = -2; _x <= 2; _x++) {
                    if (Math.abs(_x) != Math.abs(_y) && _x != 0 && _y != 0) {
                        if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                            if (fieldClone[_y + y][_x + x] != null && fieldClone[_y + y][_x + x] != Figure.NONE)
                                return null;
                            fieldClone[_y + y][_x + x] = Figure.NONE;
                        }
                    }
                }
            }
        }
        fieldClone[y][x] = figure;
        //Main.printField(fieldClone);
        return fieldClone;
    }

    private Figure[][] arrCopy(Figure[][] arr) {
        Figure[][] newArr = new Figure[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, newArr[i], 0, arr[0].length);
        }
        return newArr;
    }
}
