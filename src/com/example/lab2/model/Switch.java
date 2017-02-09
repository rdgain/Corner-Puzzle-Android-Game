package com.example.lab2.model;

import com.example.lab2.Constants;
import com.example.lab2.PuzzleView;

import java.io.Serializable;

/**
 * Created by Raluca on 16-Apr-16.
 */
public class Switch extends GameObject implements Serializable {
    GameObject active;
    private boolean triggered = false;

    public Switch (CornerPuzzleModel model, int id, GameObject b) {
        game = model;
        this.id = id;
        active = b;
    }

    public boolean trigger() {
        if (triggered) return false;
        else triggered = true;
        return true;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public boolean execute(PuzzleView view) {
        triggered = true;
        id = Constants.TYPE_SWITCH_ON;
        return active.activate(view);
    }
}
