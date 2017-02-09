package com.example.lab2.model;

import com.example.lab2.PuzzleView;

import java.io.Serializable;

import static com.example.lab2.Constants.PLAYER_POS;
import static com.example.lab2.Constants.TYPE_DEAD;

/**
 * Created by Raluca on 16-Apr-16.
 */
public class Hole extends GameObject implements Serializable {

    public Hole(CornerPuzzleModel model, int id) {
        this.id = id;
        game = model;
    }

    @Override
    public boolean activate(PuzzleView view) {
        id = TYPE_DEAD; //delete
        view.postInvalidate();
        return true;
    }

    @Override
    public boolean execute(PuzzleView view) {
        game.player.interrupt();
        game.player.setPos(new Vector2D(PLAYER_POS));

        return true;
    }
}
