package com.example.lab2.model;

import com.example.lab2.Constants;
import com.example.lab2.PuzzleView;

import java.io.Serializable;

/**
 * Created by Raluca on 21-Mar-16.
 */
public class ConditionalSwitch extends GameObject implements Serializable {
    Collectible passive;
    GameObject active;
    private boolean triggered = false;

    public ConditionalSwitch (CornerPuzzleModel model, int id, Collectible c, GameObject b) {
        game = model;
        this.id = id;
        passive = c;
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
        if (game.player.removeItem(passive)) {
            triggered = true;
            if (id == Constants.TYPE_SWITCH_OFF)
                id = Constants.TYPE_SWITCH_ON;
            return active.activate(view);
        }
        return false;
    }
}
