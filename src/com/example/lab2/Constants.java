package com.example.lab2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.lab2.model.Vector2D;

/**
 * Created by Raluca on 07-Mar-16.
 */
public abstract class Constants {
    public static final int NO_LAYERS = 2;
    public static final int NO_LEVELS = 3;
    public static final int GAME_UNITS = 4;
    public static final int GRID_SIZE = 18;
    public static final int WORLD_SIZE = GRID_SIZE * GAME_UNITS; // objects can move more freely
    public static final Vector2D PLAYER_POS = new Vector2D(0, (PuzzleView.gridSize * 3) / 4);
    public static final int SPRITE_SIZE = 32;
    public static final int NO_OBJECTS = 3;
    public static final boolean TREES_ON = true;

    //hidden objects
    public static final int TYPE_BACKPACK = -2;
    public static final int TYPE_DEAD = -1;
    //visible objects
    public static final int TYPE_PLAYER = 0;
    public static final int TYPE_CHAR1 = 25;
    public static final int TYPE_LEVEL_EXIT = 1;
    public static final int TYPE_ROCK = 2;
    public static final int TYPE_ROCK_3 = 20;
    public static final int TYPE_ROCKS = 21;
    public static final int TYPE_ROCKS_3 = 22;
    public static final int TYPE_GRASS = 3;
    public static final int TYPE_GRASS_2 = 16;
    public static final int TYPE_GRASS_3 = 17;
    public static final int TYPE_DIRT = 4;
    public static final int TYPE_DIRT_2 = 18;
    public static final int TYPE_DIRT_3 = 19;
    public static final int TYPE_TREE = 5;
    public static final int TYPE_LEVER_OFF = 6;
    public static final int TYPE_LEVER_ON = 7;
    public static final int TYPE_HOUSE = 8;
    public static final int TYPE_SWITCH_OFF = 9;
    public static final int TYPE_SWITCH_ON = 10;
    public static final int TYPE_SHED = 11;
    public static final int TYPE_HOLE = 23;
    //collectibles
    public static final int TYPE_NAILS = 12;
    public static final int TYPE_STRING = 13;
    public static final int TYPE_WIDGET = 14;
    public static final int TYPE_FLOWER = 15;
    public static final int TYPE_OBJ1 = 24;

}
