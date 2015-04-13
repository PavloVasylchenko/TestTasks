package com.pavlovasylchenko;

import java.util.Arrays;

public class Field {

    private Figure[][] fieldArray;

    public Field(Figure[][] fieldArray) {
        this.fieldArray = fieldArray;
    }

    public Figure[][] getFieldArray() {
        return fieldArray;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Field) {
            Figure[][] arr = Field.class.cast(obj).getFieldArray();
            if (arr.length == fieldArray.length && arr.length > 0 && arr[0].length == fieldArray[0].length) {
                for (int y = 0; y < arr.length; y++) {
                    for (int x = 0; x < arr[0].length; x++) {
                        if (arr[y][x] != fieldArray[y][x]) return false;
                    }
                }
                return true;
            }
            if (arr.length == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(fieldArray);
    }
}
