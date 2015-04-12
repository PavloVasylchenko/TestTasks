package com.pavlovasylchenko;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    int width;

    int height;

    List<Figure> figures;

    List<FigureType[][]> results = new ArrayList<>();

    public Engine(int width, int height, List<Figure> figures) {
        this.width = width;
        this.height = height;
        this.figures = figures;
    }

    public List<FigureType[][]> getResult() {
        FigureType[][] field = new FigureType[height][width];
        ArrayList<Figure> f = new ArrayList<>(figures.size());
        f.addAll(figures);
        process(0, 0, f, field);
        return results;
    }

    private void process(int y, //Позиция y
                         int x, //Позиция x
                         List<Figure> figuresLeft, //Фигуры в очереди
                         FigureType[][] nextField //Частично заполненное поле с заполненными NONE
    ) {
        //System.out.println("[" + y + ":" + x + "]");
        //printField(nextField);
        Figure figure = figuresLeft.remove(0);
        if (nextField[y][x] == null) {
            //Можно ставить
            FigureType[][] result = fillConstraints(y, x, figure.getFigureType(), arrCopy(nextField));
            if (result != null) {
                ArrayList<Figure> list = new ArrayList<>();
                list.addAll(figuresLeft);
                if (figuresLeft.size() == 0) {
                    results.add(result.clone());
                } else if (x < width - 1) {
                    process(y, x + 1, list, result);
                } else if (y < height - 1) {
                    process(y + 1, 0, list, result);
                }
            }
        }
        figuresLeft.add(figure);
        ArrayList<Figure> list = new ArrayList<>();
        list.addAll(figuresLeft);
        if (x < width - 1) {
            process(y, x + 1, list, nextField);
        } else if (y < height - 1) {
            process(y + 1, 0, list, nextField);
        }
        //Пробуем ставить
        //Если получилось то вызываем с меньшим количеством
        //если не получилось то x+1 или y+1
    }

    private FigureType[][] fillConstraints(int y, int x, FigureType figureType, FigureType[][] fieldClone) {
        int val = height;
        if (width > height) val = width;
        if (figureType == FigureType.QUEEN) {
            for (int i = 0; i < val; i++) {
                if (i < height) {
                    if (fieldClone[i][x] != null && fieldClone[i][x] != FigureType.NONE) return null;
                    fieldClone[i][x] = FigureType.NONE;
                }

                if (i < width) {
                    if (i < width - 1 && fieldClone[y][i] != null && fieldClone[y][i] != FigureType.NONE) return null;
                    fieldClone[y][i] = FigureType.NONE;
                }

                if (y + i < height && x + i < width) {
                    if (fieldClone[y + i][x + i] != null && fieldClone[y + i][x + i] != FigureType.NONE) return null;
                    fieldClone[y + i][x + i] = FigureType.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (fieldClone[y - i][x - i] != null && fieldClone[y - i][x - i] != FigureType.NONE) return null;
                    fieldClone[y - i][x - i] = FigureType.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (fieldClone[y + i][x - i] != null && fieldClone[y + i][x - i] != FigureType.NONE) return null;
                    fieldClone[y + i][x - i] = FigureType.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (fieldClone[y - i][x + i] != null && fieldClone[y - i][x + i] != FigureType.NONE) return null;
                    fieldClone[y - i][x + i] = FigureType.NONE;
                }
            }
        }
        if (figureType == FigureType.ROOK) {
            for (int i = 0; i < val; i++) {
                if (i < height) {
                    if (fieldClone[i][x] != null && fieldClone[i][x] != FigureType.NONE) return null;
                    fieldClone[i][x] = FigureType.NONE;
                }

                if (i < width) {
                    if (fieldClone[y][i] != null && fieldClone[y][i] != FigureType.NONE) return null;
                    fieldClone[y][i] = FigureType.NONE;
                }
            }
        }
        if (figureType == FigureType.BISHOP) {
            for (int i = 0; i < val; i++) {
                if (y + i < height && x + i < width) {
                    if (fieldClone[y + i][x + i] != null && fieldClone[y + i][x + i] != FigureType.NONE) return null;
                    fieldClone[y + i][x + i] = FigureType.NONE;
                }

                if (y - i >= 0 && x - i >= 0) {
                    if (fieldClone[y - i][x - i] != null && fieldClone[y - i][x - i] != FigureType.NONE) return null;
                    fieldClone[y - i][x - i] = FigureType.NONE;
                }

                if (y + i < height && x - i >= 0) {
                    if (fieldClone[y + i][x - i] != null && fieldClone[y + i][x - i] != FigureType.NONE) return null;
                    fieldClone[y + i][x - i] = FigureType.NONE;
                }

                if (y - i >= 0 && x + i < width) {
                    if (fieldClone[y - i][x + i] != null && fieldClone[y - i][x + i] != FigureType.NONE) return null;
                    fieldClone[y - i][x + i] = FigureType.NONE;
                }
            }
        }
        if (figureType == FigureType.KING) {
            int i = 1;
            if (fieldClone[i][x] != null && fieldClone[i][x] != FigureType.NONE) return null;
            fieldClone[i][x] = FigureType.NONE;

            if (fieldClone[y][i] != null && fieldClone[y][i] != FigureType.NONE) return null;
            fieldClone[y][i] = FigureType.NONE;

            if (y + i < height && x + i < width) {
                if (fieldClone[y + i][x + i] != null && fieldClone[y + i][x + i] != FigureType.NONE) return null;
                fieldClone[y + i][x + i] = FigureType.NONE;
            }

            if (y - i >= 0 && x - i >= 0) {
                if (fieldClone[y - i][x - i] != null && fieldClone[y - i][x - i] != FigureType.NONE) return null;
                fieldClone[y - i][x - i] = FigureType.NONE;
            }

            if (y + i < height && x - i >= 0) {
                if (fieldClone[y + i][x - i] != null && fieldClone[y + i][x - i] != FigureType.NONE) return null;
                fieldClone[y + i][x - i] = FigureType.NONE;
            }

            if (y - i >= 0 && x + i < width) {
                if (fieldClone[y - i][x + i] != null && fieldClone[y - i][x + i] != FigureType.NONE) return null;
                fieldClone[y - i][x + i] = FigureType.NONE;
            }
        }
        if (figureType == FigureType.KNIGHT) {
            for (int _y = -2; _y <= 2; _y++) {
                for (int _x = -2; _x <= 2; _x++) {
                    if (Math.abs(_x) != Math.abs(_y) && _x != 0 && _y != 0) {
                        if (_y + y >= 0 && _x + x >= 0 && _y + y < height && _x + x < width) {
                            fieldClone[_y + y][_x + x] = FigureType.NONE;
                        }
                    }
                }
            }
        }
        fieldClone[y][x] = figureType;
        return fieldClone;
    }

    private FigureType[][] arrCopy(FigureType[][] arr) {
        FigureType[][] newArr = new FigureType[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            System.arraycopy(arr[i], 0, newArr[i], 0, arr[0].length);
        }
        return newArr;
    }
}
