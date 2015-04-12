package com.pavlovasylchenko;

import java.util.Arrays;

public class Field {

    private Figure[][] field;

    public Field(Figure[][] field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Figure[][]) {
            return Arrays.equals(field, Figure[][].class.cast(obj));
        }
        return false;
    }
}
