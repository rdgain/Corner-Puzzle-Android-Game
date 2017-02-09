package com.example.lab2.model;

import static com.example.lab2.Constants.*;
import com.example.lab2.PuzzleView;
import com.example.lab2.model.GameObject;

import java.io.Serializable;

/**
 * Created by Raluca on 14-Mar-16.
 * id = 3 : off
 * id = 4 : on
 */
public class Lever extends GameObject implements Serializable {
    GameObject active;

    public Lever(GameObject o) {
        id = TYPE_LEVER_OFF;
        active = o;
    }

    @Override
    public boolean execute(PuzzleView view) {
        if (id == TYPE_LEVER_OFF) {
            id = TYPE_LEVER_ON;
            if (active != null) {
                active.activate(view);
                return true;
            }
        } else {
            id = TYPE_LEVER_OFF;
        }
        view.postInvalidate();
        return false;
    }

    public String toString () {
        return ""+id;
    }

}
