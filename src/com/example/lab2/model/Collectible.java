package com.example.lab2.model;

import com.example.lab2.Constants;
import com.example.lab2.PuzzleView;

/**
 * Created by Raluca on 21-Mar-16.
 */
public class Collectible extends GameObject {
    boolean collected;
    CornerPuzzleModel game;

    public Collectible (CornerPuzzleModel model, int id) {
        collected = false;
        this.id = id;
        game = model;
    }

    /**
     * Method to collect this item.
     * @param view - view object
     * @return - true if item successfully collected, false otherwise.
     */
    @Override
    public boolean execute(PuzzleView view) {
        //id = Constants.TYPE_DEAD;
        if (game.player.addItem(this)) {
            collected = true;
            return true;
        }
        return false;
    }
}
