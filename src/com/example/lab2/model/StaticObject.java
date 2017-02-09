package com.example.lab2.model;

import static com.example.lab2.Constants.*;
import com.example.lab2.PuzzleView;

import java.io.Serializable;

/**
 * Created by Raluca on 14-Mar-16.
 */
public class StaticObject extends GameObject implements Serializable {
    public StaticObject(CornerPuzzleModel model, int id) {
        this.id = id;
        game = model;
    }

    @Override
    public boolean activate(PuzzleView view) {
        id = TYPE_DEAD; //delete
        view.postInvalidate();
        return true;
    }

    public String toString () {
        return ""+id;
    }
}
