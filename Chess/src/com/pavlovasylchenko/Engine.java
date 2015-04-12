package com.pavlovasylchenko;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    int width;

    int height;

    List<Figure> figures;
    AtomicInteger wins = new AtomicInteger(0);
    Set<Field> results = new HashSet<>();

    ExecutorService executor;

    public Engine(int width, int height, List<Figure> figures) {
        this.width = width;
        this.height = height;
        this.figures = figures;
    }

    public Set<Field> getResult(boolean multithreading) {
        executor = Executors.newFixedThreadPool(4);
        final List<Figure> figuresList = new ArrayList<>();
        figuresList.addAll(figures);
        final Figure fig = figuresList.remove(0);
        final Figure[] f = figuresList.toArray(new Figure[figuresList.size()]);
        List<Future<?>> futures = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Figure[][] field = new Figure[height][width];
                field = fillConstraints(y, x, fig, field);
                final Figure[][] finalField = field;
                if (multithreading) {
                    futures.add(executor.submit(new Runnable() {
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
        if (multithreading) {
            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Всего вариантов с повторениями " + wins);
        executor.shutdown();
        return results;
    }

    private void process(int y, //Позиция y
                         int x, //Позиция x
                         Figure[] figuresLeft, //Фигуры в очереди
                         Figure[][] nextField //Частично заполненное поле с заполненными NONE
    ) {
        final Figure figure = figuresLeft[0];
        if (nextField[y][x] == null) {
            //Можно ставить, ставим фигуру и запускаем отдельную ветку вычислений
            final Figure[][] result = fillConstraints(y, x, figure, arrCopy(nextField));
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
        //Перебираем все поле
        if (x < width - 1) {
            process(y, x + 1, figuresLeft, nextField);
        } else if (y < height - 1) {
            process(y + 1, 0, figuresLeft, nextField);
        }
    }

    // В места которые нельзя ставить фигуры ставим NONE, фигуры ставить можно только в места null.
    private Figure[][] fillConstraints(int y, int x, Figure figure, Figure[][] field) {
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

    private boolean checkKnight(int y, int x, Figure[][] field) {
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

    private boolean checkKing(int y, int x, Figure[][] field) {
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

    private boolean checkBishop(int y, int x, Figure[][] field) {
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

    private boolean checkRook(int y, int x, Figure[][] field) {
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

    private Figure[][] arrCopy(Figure[][] arr) {
        Figure[][] newArr = new Figure[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, newArr[i], 0, arr[0].length);
        }
        return newArr;
    }
}
