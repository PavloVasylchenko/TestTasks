package com.pavlovasylchenko;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Engine {

    int width;

    int height;

    Figure[] figures;
    AtomicInteger wins = new AtomicInteger(0);
    List<Field> results = new ArrayList<>();


    List<Future<?>> futures = new ArrayList<>();
    ExecutorService executor;

    public Engine(int width, int height, List<Figure> figures) {
        this.width = width;
        this.height = height;
        this.figures = figures.toArray(new Figure[figures.size()]);
    }

    public List<Field> getResult(boolean multithreading) {
        executor = Executors.newFixedThreadPool(4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                final Figure[][] finalField = fillConstraints(y, x, figures[0], new Figure[height][width]);
                if (multithreading) {
                    final int finalX = x;
                    final int finalY = y;
                    futures.add(executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            processBranch(finalY, finalX, 0, finalField);
                        }
                    }));
                } else {
                    processBranch(y, x, 0, finalField);
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
        //System.out.println("Всего вариантов: " + wins.get());
        executor.shutdown();
        return results;
    }

    /**
     * Основной метод, вызывается рекурсивно и вытается ставить фигуры. Если фигура поставилась то
     * добавляем к позиции +1 и запускаем выччисления отдельной веткой
     * @param y
     * @param x
     * @param position
     * @param nextField
     */
    private void process(int y, //Позиция y
                         int x, //Позиция x
                         int position,
                         Figure[][] nextField //Частично заполненное поле с заполненными NONE
    ) {
//        if (position > figures.length - 1) {
//            wins.incrementAndGet();
//            return;
//        }
        if (nextField[y][x] == null) {
            //Можно ставить, ставим фигуру и запускаем отдельную ветку вычислений
            final Figure[][] result = fillConstraints(y, x, figures[position], arrCopy(nextField));
            if (result != null) {
                if (position > figures.length - 2) {
                    synchronized (Engine.class) {
                       results.add(new Field(result));
                    }
                    wins.incrementAndGet();
                } else {
                    processBranch(y, x, position, result);
                }
            }
        }
        //Перебираем все поле
        if (x < width - 1) {
            process(y, x + 1, position, nextField);
        } else if (y < height - 1) {
            process(y + 1, 0, position, nextField);
        }
    }

    /**
     * После того как поставили фигуру запускаем отдельную ветку вычислений
     * @param y
     * @param x
     * @param position позиция указывющаяя на текущий элемент в скиске фигур которые нужно поставить
     * @param result положение доски
     */
    private void processBranch(int y, int x, int position, Figure[][] result) {
        if (figures.length > position + 1 && figures[position] == figures[position + 1]) {
            if (x < width - 1) {
                process(y, x + 1, position + 1, result);
            } else if (y < height - 1) {
                process(y + 1, 0, position + 1, result);
            }
        } else {
            process(0, 0, position + 1, result);
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
