package com.example.lab2.model;

import com.example.lab2.Constants;
import com.example.lab2.PuzzleView;

import java.io.Serializable;

/**
 * Created by Raluca on 21-Mar-16.
 */
public class LevelExit extends GameObject implements Serializable {
    private boolean triggered;

    public LevelExit () {triggered = false; id = Constants.TYPE_LEVEL_EXIT ;}

    @Override
    public boolean execute(PuzzleView view) {
        setTriggered(true);
        return true;
    }

    public void setTriggered(boolean t) {triggered = t;}
    public boolean isTriggered() {return triggered;}
}
