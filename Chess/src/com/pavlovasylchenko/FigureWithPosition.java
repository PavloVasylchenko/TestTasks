package com.pavlovasylchenko;

public class FigureWithPosition {

    int posX;

    int posY;

    FigureType figureType;

    public FigureWithPosition(int posY, int posX, FigureType figureType) {
        this.posX = posX;
        this.posY = posY;
        this.figureType = figureType;
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

    public FigureType getFigureType() {
        return figureType;
    }

    public void setFigureType(FigureType figureType) {
        this.figureType = figureType;
    }
}
