package com.pavlovasylchenko;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    int width;

    int height;

    List<Figure> figures;
    //int iter = 0;
    AtomicInteger wins = new AtomicInteger(0);
    Set<Field> results = new HashSet<>();

    public Engine(int width, int height, List<Figure> figures) {
        this.width = width;
        this.height = height;
        this.figures = figures;
    }

    public Set<Field> getResult(boolean multi) {
        List<Figure> figuresList = new ArrayList<>();
        figuresList.addAll(figures);
        Figure fig = figuresList.remove(0);
        Figure[] f = figuresList.toArray(new Figure[figuresList.size()]);
        List<Thread> threads = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Figure[][] field = new Figure[height][width];
                field = fillConstraints(y, x, fig, field);
                final Figure[][] finalField = field;
                if(multi) {
                    threads.add(new Thread(new Runnable() {
                        @Override
                        public void run() {
                            process(0, 0, f, finalField);
                        }
                    }));
                } else {
                    process(0, 0, f, finalField);
                }
            }
        }
        if(multi) {
            for (Thread thrd : threads) {
                thrd.start();
            }
            for (Thread thrd : threads) {
                try {
                    thrd.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Всего вариантов с повторениями " + wins);
        return results;
    }

    private void process(int y, //Позиция y
                         int x, //Позиция x
                         Figure[] figuresLeft, //Фигуры в очереди
                         Figure[][] nextField //Частично заполненное поле с заполненными NONE
    ) {
        Figure figure = figuresLeft[0];
        if (nextField[y][x] == null) {
            //Можно ставить
            Figure[][] result = fillConstraints(y, x, figure, arrCopy(nextField));
            if (result != null) {
                if (figuresLeft.length == 1) {
                    synchronized (Engine.class) {
                        results.add(new Field(result));
                    }
                    wins.incrementAndGet();
                } else {
                    process(0, 0, Arrays.copyOfRange(figuresLeft, 1, figuresLeft.length), result);
                }
            }
        }
        if (x < width - 1) {
            process(y, x + 1, figuresLeft, nextField);
        } else if (y < height - 1) {
            process(y + 1, 0, figuresLeft, nextField);
        }
    }

    //Немного страшный метод который не хочется делить потому что тогда непонятно что он делает :)
    private Figure[][] fillConstraints(int y, int x, Figure figure, Figure[][] field) {
        int val = height;
        if (width > height) val = width;
        if (figure == Figure.QUEEN) {
            for (int i = 0; i < height; i++) {
                if (field[i][x] != null && field[i][x] != Figure.NONE) return null;
                field[i][x] = Figure.NONE;
            }
            for (int i = 0; i < width; i++) {
                if (i < width) {
                    if (field[y][i] != null && field[y][i] != Figure.NONE) return null;
                    field[y][i] = Figure.NONE;
                }
            }
            for (int i = 0; i < val; i++) {
                if (y + i < height && x + i < width) {
                    if (field[y + i][x + i] != null && field[y + i][x + i] != Figure.NONE) return null;
                    field[y + i][x + i] = Figure.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (field[y - i][x - i] != null && field[y - i][x - i] != Figure.NONE) return null;
                    field[y - i][x - i] = Figure.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (field[y + i][x - i] != null && field[y + i][x - i] != Figure.NONE) return null;
                    field[y + i][x - i] = Figure.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (field[y - i][x + i] != null && field[y - i][x + i] != Figure.NONE) return null;
                    field[y - i][x + i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.ROOK) {
            for (int i = 0; i < height; i++) {
                if (field[i][x] != null && field[i][x] != Figure.NONE) return null;
                field[i][x] = Figure.NONE;
            }
            for (int i = 0; i < width; i++) {
                if (i < width) {
                    if (field[y][i] != null && field[y][i] != Figure.NONE) return null;
                    field[y][i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.BISHOP) {
            for (int i = 0; i < val; i++) {
                if (y + i < height && x + i < width) {
                    if (field[y + i][x + i] != null && field[y + i][x + i] != Figure.NONE) return null;
                    field[y + i][x + i] = Figure.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (field[y - i][x - i] != null && field[y - i][x - i] != Figure.NONE) return null;
                    field[y - i][x - i] = Figure.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (field[y + i][x - i] != null && field[y + i][x - i] != Figure.NONE) return null;
                    field[y + i][x - i] = Figure.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (field[y - i][x + i] != null && field[y - i][x + i] != Figure.NONE) return null;
                    field[y - i][x + i] = Figure.NONE;
                }
            }
        }
        if (figure == Figure.KING) {
            for (int _y = -1; _y <= 1; _y++) {
                for (int _x = -1; _x <= 1; _x++) {
                    if (_y == 0 && _x == 0) continue;
                    if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                        if (field[_y + y][_x + x] != null && field[_y + y][_x + x] != Figure.NONE) return null;
                        field[_y + y][_x + x] = Figure.NONE;
                    }
                }
            }
        }
        if (figure == Figure.KNIGHT) {
            for (int _y = -2; _y <= 2; _y++) {
                for (int _x = -2; _x <= 2; _x++) {
                    if (Math.abs(_x) != Math.abs(_y) && _x != 0 && _y != 0) {
                        if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                            if (field[_y + y][_x + x] != null && field[_y + y][_x + x] != Figure.NONE)
                                return null;
                            field[_y + y][_x + x] = Figure.NONE;
                        }
                    }
                }
            }
        }
        field[y][x] = figure;
        //Main.printField(fieldClone);
        return field;
    }

    private Figure[][] arrCopy(Figure[][] arr) {
        Figure[][] newArr = new Figure[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, newArr[i], 0, arr[0].length);
        }
        return newArr;
    }
}
