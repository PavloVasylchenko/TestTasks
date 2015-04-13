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
                final Map<Integer, Figure> busy = fillConstraints(y, x, figures[0], new HashMap<Integer, Figure>());
                if (multithreading) {
                    final int finalX = x;
                    final int finalY = y;
                    futures.add(executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            if (figures.length > 1 && figures[0] == figures[1]) {
                                if (finalX < width - 1) {
                                    process(finalY, finalX + 1, 1, busy);
                                } else if (finalY < height - 1) {
                                    process(finalY + 1, 0, 1, busy);
                                }
                            } else {
                                process(0, 0, 1, busy);
                            }
                        }
                    }));
                } else {
                    if (figures.length > 1 && figures[0] == figures[1]) {
                        if (x < width - 1) {
                            process(y, x + 1, 1, busy);
                        } else if (y < height - 1) {
                            process(y + 1, 0, 1, busy);
                        }
                    } else {
                        process(0, 0, 1, busy);
                    }
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
        System.out.println("Всего вариантов: " + wins.get());
        executor.shutdown();
        return results;
    }

    private void process(int y, //Позиция y
                         int x, //Позиция x
                         int position,
                         Map<Integer, Figure> busy
    ) {
//        if (position > figures.length - 1) {
//            wins.incrementAndGet();
//            return;
//        }
        if (!busy.containsKey(x + y * 1000)) {
            //Можно ставить, ставим фигуру и запускаем отдельную ветку вычислений
            final Map<Integer, Figure> result = fillConstraints(y, x, figures[position], busy);
            if (result != null) {
                Map<Integer, Figure> newBusy = new HashMap<>();
                newBusy.putAll(busy);
                newBusy.putAll(result);
                if (position > figures.length - 2) {
                    //synchronized (Engine.class) {
                    //   results.add(new Field(result));
                    //}
                    wins.incrementAndGet();
                } else {
                    if (figures.length > position + 1 && figures[position] == figures[position + 1]) {
                        if (x < width - 1) {
                            process(y, x + 1, position + 1, newBusy);
                        } else if (y < height - 1) {
                            process(y + 1, 0, position + 1, newBusy);
                        }
                    } else {
                        process(0, 0, position + 1, newBusy);
                    }
                }
            }
        }
        //Перебираем все поле
        if (x < width - 1) {
            process(y, x + 1, position, busy);
        } else if (y < height - 1) {
            process(y + 1, 0, position, busy);
        }
    }

    // В места которые нельзя ставить фигуры ставим NONE, фигуры ставить можно только в места null.
    private Map<Integer, Figure> fillConstraints(int y, int x, Figure figure, Map<Integer, Figure> busy) {
        Map<Integer, Figure> result = new HashMap<>();
        switch (figure) {
            case QUEEN: {
                if (!checkRook(y, x, busy, result) || !checkBishop(y, x, busy, result)) return null;
                break;
            }
            case ROOK: {
                if (!checkRook(y, x, busy, result)) return null;
                break;
            }
            case BISHOP: {
                if (!checkBishop(y, x, busy, result)) return null;
                break;
            }
            case KING: {
                if (!checkKing(y, x, busy, result)) return null;
                break;
            }
            case KNIGHT: {
                if (!checkKnight(y, x, busy, result)) return null;
                break;
            }
        }
        result.put(x + y * 1000, figure);
        return result;
    }

    private boolean checkKnight(int y, int x, Map<Integer, Figure> busy, Map<Integer, Figure> result) {
        for (int _y = -2; _y <= 2; _y++) {
            for (int _x = -2; _x <= 2; _x++) {
                if (Math.abs(_x) != Math.abs(_y) && _x != 0 && _y != 0) {
                    if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                        Integer point = (_x + x) + (_y + y) * 1000;
                        Figure f = busy.get(point);
                        if (f == null) {
                            result.put(point, Figure.NONE);
                        } else {
                            if (f == Figure.NONE) result.put(point, Figure.NONE);
                            else return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean checkKing(int y, int x, Map<Integer, Figure> busy, Map<Integer, Figure> result) {
        for (int _y = -1; _y <= 1; _y++) {
            for (int _x = -1; _x <= 1; _x++) {
                if (_y == 0 && _x == 0) continue;
                if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                    Integer point = (_x + x) + (_y + y) * 1000;
                    Figure f = busy.get(point);
                    if (f == null) {
                        result.put(point, Figure.NONE);
                    } else {
                        if (f == Figure.NONE) result.put(point, Figure.NONE);
                        else return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkBishop(int y, int x, Map<Integer, Figure> busy, Map<Integer, Figure> result) {
        final int val = width > height ? width : height;
        for (int i = 1; i < val; i++) {
            if (y + i < height && x + i < width) {
                Integer point = (x + i) + (y + i) * 1000;
                Figure f = busy.get(point);
                if (f == null) {
                    result.put(point, Figure.NONE);
                } else {
                    if (f == Figure.NONE) result.put(point, Figure.NONE);
                    else return false;
                }
            }

            if (y - i >= 0 && x - i >= 0) {
                Integer point = (x - i) + (y - i) * 1000;
                Figure f = busy.get(point);
                if (f == null) {
                    result.put(point, Figure.NONE);
                } else {
                    if (f == Figure.NONE) result.put(point, Figure.NONE);
                    else return false;
                }
            }

            if (y + i < height && x - i >= 0) {
                Integer point = (x - i) + (y + i) * 1000;
                Figure f = busy.get(point);
                if (f == null) {
                    result.put(point, Figure.NONE);
                } else {
                    if (f == Figure.NONE) result.put(point, Figure.NONE);
                    else return false;
                }
            }

            if (y - i >= 0 && x + i < width) {
                Integer point = (x + i) + (y - i) * 1000;
                Figure f = busy.get(point);
                if (f == null) {
                    result.put(point, Figure.NONE);
                } else {
                    if (f == Figure.NONE) result.put(point, Figure.NONE);
                    else return false;
                }
            }
        }
        return true;
    }

    private boolean checkRook(int y, int x, Map<Integer, Figure> busy, Map<Integer, Figure> result) {
        for (int i = 0; i < height; i++) {
            if(i == y) continue;
            Integer point = x + i * 1000;
            Figure f = busy.get(point);
            if (f == null) {
                result.put(point, Figure.NONE);
            } else {
                if (f == Figure.NONE) result.put(point, Figure.NONE);
                else return false;
            }
        }
        for (int i = 0; i < width; i++) {
            if(i == x) continue;
            Integer point = i + y * 1000;
            Figure f = busy.get(point);
            if (f == null) {
                result.put(point, Figure.NONE);
            } else {
                if (f == Figure.NONE) result.put(point, Figure.NONE);
                else return false;
            }
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
