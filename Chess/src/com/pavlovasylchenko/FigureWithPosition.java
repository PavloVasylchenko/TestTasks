package com.pavlovasylchenko;

public class FigureWithPosition {

    int posX;

    int posY;

    Figure figure;

    public FigureWithPosition(int posY, int posX, Figure figure) {
        this.posX = posX;
        this.posY = posY;
        this.figure = figure;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }
}
