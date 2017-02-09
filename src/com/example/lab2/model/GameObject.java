package com.example.lab2.model;

import com.example.lab2.Constants;
import com.example.lab2.PuzzleView;

import java.io.Serializable;

/**
 * Created by Raluca on 07-Mar-16.
 */
public abstract class GameObject implements Serializable {
    Vector2D pos;
    boolean solved = false;
    public int id = Constants.TYPE_PLAYER;//default
    CornerPuzzleModel game;

    public void setPos (Vector2D to) {
        pos = to;
    }

    public Vector2D getPos () {
        return pos;
    }

    //called when user taps on it
    public boolean execute (PuzzleView view) {return true;}

    //called by other objects
    public boolean activate (PuzzleView view) {return true;}

    public void update() { }
}
