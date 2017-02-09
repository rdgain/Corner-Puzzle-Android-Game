package com.example.lab2.model;

import android.content.res.Configuration;
import com.example.lab2.*;
import com.example.lab2.model.navigation.NavMesh;
import com.example.lab2.model.navigation.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.example.lab2.Constants.*;

/**
 * Created by Raluca on 16-Feb-16.
 */

public class CornerPuzzleModel implements Serializable {
    public transient PuzzleModel modelH;
    public transient PuzzleModel modelV;
    public int level;
    public boolean nextLevel, end;
    int t;
    int orientation;
    ArrayList<GameObject[][]> layers;
    LevelExit exit;
    public Player player;
    NavMesh nm;

    public CornerPuzzleModel(int t, PuzzleView view) {
        this.t = t;
        level = 0;
        nextLevel = true;
        end = false;
        reset(view);
    }

    public boolean execute (int j, int i, int o, PuzzleView view) {
        if (nextLevel) {
            nextLevel = false;
            if (level == 3) end = true;
        }
        else if (end) System.exit(1);
        else {
            GameObject g = getGameObject(j, i, o);
            if (g != null && !(g instanceof LevelExit) && !(g instanceof Collectible) && !(g instanceof ConditionalSwitch) && !(g instanceof Hole)) {
                return g.execute(view);
            } else {
                Vector2D curPos = player.getPos();
                if (o == Configuration.ORIENTATION_LANDSCAPE && (getPlayerOrientation() == 0 || getPlayerOrientation() == 2)) {
                    Vector2D pos = translateFromHorizontal(i, j);
                    if (checkInGrid(pos)) {
                        if (pos.x == curPos.x && pos.y == curPos.y)
                            return player.execute(view);
                        else
                            player.setPos(pos, view, nm);
                    }
                } else if (o == Configuration.ORIENTATION_PORTRAIT && (getPlayerOrientation() == 1 || getPlayerOrientation() == 2)) {
                    Vector2D pos = translateFromVertical(i, j);
                    if (checkInGrid(pos)){
                        if (pos.x == curPos.x && pos.y == curPos.y)
                            return player.execute(view);
                        else
                            player.setPos(pos, view, nm);
                    }
                }
            }
            return true;
        }
        return true;
    }

    public GameObject getGameObject(int x, int y, int o) {
        if (o == Configuration.ORIENTATION_PORTRAIT) {
            try {
                return modelV.layers.get(1)[x][y];
            } catch (Exception e) {
                return null;
            }
        } else if (o == Configuration.ORIENTATION_LANDSCAPE) {
            try {
                return modelH.layers.get(1)[x][y];
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    //return 0 if landscape, 1 if portrait, 2 if both, -1 if neither
    public int getPlayerOrientation () {
        return 2;
    }

    /**
     * Methods to translate coordinates between the different models.
     */

    public Vector2D translateToHorizontal (int x, int y) {
        Vector2D p = new Vector2D();
        p.x = x;
        p.y = y - t/2;
        return p;
    }

    public Vector2D translateFromHorizontal (int x, int y) {
        Vector2D p = new Vector2D();
        p.x = x;
        p.y = y + t/2;
        return p;
    }

    public Vector2D translateToVertical (int x, int y) {
        Vector2D p = new Vector2D();
        p.x = x - t/2;
        p.y = y;
        return p;
    }

    public Vector2D translateFromVertical (int x, int y) {
        Vector2D p = new Vector2D();
        p.x = x + t/2;
        p.y = y;
        return p;
    }

    /**
     * Updates game objects.
     */
    void update() { }

    //check if there is no game object at position x, y
    //include game objects which should be ignored and walked over.
    boolean checkClear(int x, int y) {
        try {
            GameObject o = layers.get(1)[x][y];
            return x < PuzzleView.gridSize && y < PuzzleView.gridSize &&
                    (o == null || o instanceof LevelExit || o instanceof Collectible || o instanceof ConditionalSwitch || o instanceof Hole)
                    && layers.get(0)[x][y] != null;
        } catch (Exception e) {
            return false;
        }
    }

    boolean checkInGrid(Vector2D v) {
        int x = v.x;
        int y = v.y;
        GameObject o1 = null, o2 = null;
        try {
            o1 = layers.get(1)[x][y];
        } catch (Exception ignored) {}
        try {
            o2 = layers.get(0)[x][y];
        } catch (Exception ignored) {}
        return o1 != null || o2 != null;
    }

    //check if vertical and horizontal solved
    boolean isSolved () {
        return exit.isTriggered();
    }

    /**
     * Resets the game state.
     * @param view
     */
    public void reset(PuzzleView view) {
        exit = new LevelExit();
        Storage inv = null;
        if (player != null)
            inv = player.inventory;
        player = new Player(PLAYER_POS, this);
        if (inv != null)
            player.inventory = inv;
        modelH = new PuzzleModel(0,t);
        modelV = new PuzzleModel(1,t);
        constructGrid(view, level);
        createNavMesh();
    }

    /**
     * Updates models and game state.
     * @param view - view object
     */
    public void updateModels(PuzzleView view) {
        //check if player is standing on an object that should be triggered
        if (player != null) {
            int x = player.getPos().x;
            int y = player.getPos().y;
            GameObject o = layers.get(1)[y][x];
            if (o != null) {
                o.execute(view);
            }
        }

        //check end of game
        if (isSolved()) {
            System.out.println("Finished level.");
            if (level == NO_LEVELS ) {
                nextLevel = true;
                end = true;
            } else {
                nextLevel = true;
                level++;
                reset(view);
            }
        }

        //update objects
        for (int i = 0; i < t; i++)
            for (int j = 0; j < t; j++) {
                //delete dead objects and recreate the navigation mesh
                GameObject o = layers.get(1)[i][j];
                if (o != null && (o.id == TYPE_DEAD || o instanceof Collectible && ((Collectible)o).collected)) {
                    layers.get(1)[i][j] = null;
                    createNavMesh();
                }
            }

        //update horizontal
        for (int i = t/2; i < t; i++)
            for (int j = 0; j < t; j++) {
                modelH.layers.get(0)[i - t / 2][j] = layers.get(0)[i][j];
                modelH.layers.get(1)[i - t / 2][j] = layers.get(1)[i][j];
            }

        //update vertical
        for (int i = 0; i < t; i++)
            for (int j = t/2; j < t; j++) {
                modelV.layers.get(0)[i][j - t / 2] = layers.get(0)[i][j];
                modelV.layers.get(1)[i][j - t / 2] = layers.get(1)[i][j];
            }
    }

    /**
     * Creates game objects
     * @param view - view object
     * @param level - level index
     */
    void createObjects(PuzzleView view, int level) {
        loadLevel(level);
        updateModels(view);
    }

    /**
     * Loads the corresponding level of the game.
     * @param idx - level index
     */
    void loadLevel(int idx) {
        boolean treesOn = TREES_ON;

        switch (idx) {
            case 0:
                //level 1

                //horizontal model
                createBackground(0, t/2, t/2, t, TYPE_GRASS, TYPE_DIRT);
                //objects
                //wall of rocks
                StaticObject o1 = new StaticObject(this, TYPE_ROCK);
                for (int i = t/2; i < t - 2; i++) layers.get(1)[i][3] = o1; // vertical
                for (int j = 0; j < t/2; j++) layers.get(1)[t/2 + 5][j] = o1; // horizontal
                for (int i = t-2; i < t; i++) layers.get(1)[i][t/2 - 1] = o1; // vertical
                //rocks to be triggered
                StaticObject rock1 = new StaticObject(this, TYPE_ROCK);
                StaticObject rock2 = new StaticObject(this, TYPE_ROCK);
                layers.get(1)[t/2 + 3][3] = rock1;
                layers.get(1)[t - 3][t/2 - 1] = rock2;
                //house
                layers.get(1)[t/2 + 1][0] = new StaticObject(this, TYPE_HOUSE);
                //collectible
                Collectible w = new Collectible(this, TYPE_WIDGET);
                layers.get(1)[t/2][t/2 - 2] = w;
                //lever
                layers.get(1)[t/2 + 4][1] = new Lever(rock1);
                //level exit
                layers.get(1)[t-1][2] = exit;
                exit.setPos(new Vector2D(2,t-1));
                //trees
                if (treesOn) {
                    StaticObject o2 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[t/2 + 1][1] = o2;
                    layers.get(1)[t/2][2] = o2;
                    layers.get(1)[t/2 + 4][2] = o2;
                    layers.get(1)[t - 1][0] = o2;
                    layers.get(1)[t - 1][1] = o2;
                    layers.get(1)[t - 2][0] = o2;
                    layers.get(1)[t/2 + 2][t/2 - 1] = o2;
                    layers.get(1)[t/2 + 3][t/2 - 1] = o2;
                    layers.get(1)[t - 1][t/2 - 3] = o2;
                }

                //vertical model
                createBackground(t/2, 0, t, t/2, TYPE_GRASS, TYPE_DIRT);
                //wall of rocks
                for (int i = t/2 - 5; i < t/2; i++) layers.get(1)[i][t/2 + 3] = o1; // vertical
                for (int j = t/2 + 4; j < t - 2; j++) layers.get(1)[t/2][j] = o1; // horizontal
                //objects
                layers.get(1)[1][t/2 + 3] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, w, rock2);
                layers.get(1)[t/2 - 2][t/2] = new Collectible(this, TYPE_FLOWER);
                layers.get(1)[t/2 - 3][t - 2] = new Collectible(this, TYPE_STRING);
                //trees
                if (treesOn) {
                    StaticObject o2 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[t/2][t/2] = o2;
                    layers.get(1)[t/2 + 3][t/2 + 1] = o2;
                    layers.get(1)[t - 1][t/2 + 2] = o2;
                    layers.get(1)[t/2 + 2][t-1] = o2;
                    layers.get(1)[t/2 + 3][t-1] = o2;
                    layers.get(1)[t/2 + 4][t-2] = o2;
                }

                //transition model
                createBackground(t/2, t/2, t, t, TYPE_GRASS, TYPE_DIRT);
                //objects
                layers.get(1)[t - 2][t - 2] = new Storage(TYPE_SHED, 10);
                layers.get(1)[t - 2][t/2 + 3] = new Collectible(this, TYPE_NAILS);
                //wall of rocks
                for (int i = t/2; i < t - 2; i++) // vertical
                    layers.get(1)[i][t/2 + 3] = o1;
                for (int j = t/2; j < t/2 + 3; j++) // horizontal
                    layers.get(1)[t/2 + 5][j] = o1;
                //trees
                if (treesOn) {
                    StaticObject o2 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[t/2 - 1][t/2 + 1] = o2;
                    layers.get(1)[2][t/2] = o2;
                    layers.get(1)[3][t/2] = o2;
                    layers.get(1)[2][t/2 + 1] = o2;
                    layers.get(1)[3][t/2+1] = o2;
                    layers.get(1)[1][t/2+2] = o2;
                    layers.get(1)[0][t/2+3] = o2;
                    layers.get(1)[1][t/2+4] = o2;
                    layers.get(1)[2][t-2] = o2;
                    layers.get(1)[3][t-2] = o2;
                    layers.get(1)[4][t-1] = o2;
                    layers.get(1)[4][t-1] = o2;
                    layers.get(1)[t/2-1][t-3] = o2;
                }
                break;

            case 1:
                // level 2

                //horizontal model
                createBackground(0, t/2, t/2, t, TYPE_GRASS_2, TYPE_DIRT_2);
                //objects
                //wall of rocks
                StaticObject o2 = new StaticObject(this, TYPE_ROCKS);
                for (int j = 0; j < 3; j++) layers.get(1)[t - 4][j] = o2; // horizontal
                for (int j = 2; j < 7; j++) layers.get(1)[t/2 + 2][j] = o2; // horizontal
                for (int j = 6; j < 9; j++) layers.get(1)[t - 4][j] = o2; // horizontal
                for (int j = 5; j < 9; j++) layers.get(1)[t - 2][j] = o2; // horizontal
                for (int i = t/2 + 3; i < t/2 + 5; i++) layers.get(1)[i][2] = o2; // vertical
                for (int i = t-2; i < t; i++) layers.get(1)[i][2] = o2; // vertical
                for (int i = t/2 + 3; i < t/2 + 5; i++) layers.get(1)[i][6] = o2; // vertical
                for (int i = t/2; i < t/2 + 4; i++) layers.get(1)[i][t/2 - 1] = o2; // vertical
                //rocks to be triggered
                rock1 = new StaticObject(this, TYPE_ROCKS);
                rock2 = new StaticObject(this, TYPE_ROCKS);
                StaticObject rock3 = new StaticObject(this, TYPE_ROCKS);
                StaticObject rock4 = new StaticObject(this, TYPE_ROCKS);
                layers.get(1)[t - 3][2] = rock1; // lever
                layers.get(1)[t - 3][6] = rock2; //switch with string
                //hole
                Hole hole1 = new Hole(this, TYPE_HOLE);
                //lever
                layers.get(1)[t/2 + 2][1] = new Lever(rock1);
                layers.get(1)[t/2 + 3][1] = new Lever(hole1);
                //collectibles
                Collectible c = new Collectible(this, TYPE_OBJ1);
                layers.get(1)[t - 1][0] = c;
                //switch
                layers.get(1)[t/2][t/2 - 2] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, c, rock3);
                layers.get(1)[t/2][t/2 - 3] = new Switch(this, TYPE_SWITCH_OFF, rock4);
                //trees
                if (treesOn) {
                    StaticObject o0 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[t / 2 + 3][5] = o0;
                    layers.get(1)[t / 2 + 4][4] = o0;
                    layers.get(1)[t - 1][3] = o0;
                    layers.get(1)[t / 2][0] = o0;
                }

                //vertical model
                createBackground(t/2, 0, t, t/2, TYPE_GRASS_2, TYPE_DIRT_2);
                //objects
                //walls of rocks
                for (int i = 0; i < 4; i++) layers.get(1)[i][t/2] = o2; // vertical
                for (int i = 3; i < t/2; i++) layers.get(1)[i][t/2 + 4] = o2; // vertical
                for (int i = 1; i < 5; i++) layers.get(1)[i][t - 2] = o2; // vertical
                for (int i = 4; i < t/2; i++) layers.get(1)[i][t - 3] = o2; // vertical
                for (int j = t/2; j < t/2 + 5; j++) layers.get(1)[1][j] = o2; // horizontal
                for (int j = t/2 + 2; j < t/2 + 5; j++) layers.get(1)[3][j] = o2; // horizontal
                for (int j = t/2 + 1; j < t/2 + 4; j++) layers.get(1)[t/2 - 1][j] = o2; // horizontal
                layers.get(1)[3][t-1] = o2;
                //rock
                layers.get(1)[0][t - 2] = rock4;
                //switch
                layers.get(1)[0][t/2 + 1] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, new Collectible(this,TYPE_STRING),rock2);
                //collectible
                layers.get(1)[1][t - 1] = new Collectible(this, TYPE_WIDGET);
                //trees
                if (treesOn) {
                    StaticObject o0 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[2][t - 3] = o0;
                    layers.get(1)[t / 2 - 4][t/2] = o0;
                    layers.get(1)[t/2 - 4][t/2 + 1] = o0;
                    layers.get(1)[t / 2 - 1][t - 1] = o0;
                }

                //transition model
                createBackground(t/2, t/2, t, t, TYPE_GRASS_2, TYPE_DIRT_2);
                //objects
                //walls of rocks
                for (int j = t/2; j < t/2 + 5; j++) layers.get(1)[t - 2][j] = o2; // horizontal
                for (int i = t/2; i < t - 3; i++) layers.get(1)[i][t/2 + 1] = o2; // vertical
                for (int i = t/2 + 2; i < t - 2; i++) layers.get(1)[i][t/2 + 4] = o2; // vertical
                for (int i = t - 3; i < t; i++) layers.get(1)[i][t - 3] = o2; // vertical
                for (int i = t/2 + 2; i < t - 2; i++) layers.get(1)[i][t - 2] = o2; // vertical
                layers.get(1)[t - 4][t/2] = o2;
                layers.get(1)[t/2][t - 3] = o2;
                layers.get(1)[t/2 + 2][t - 4] = o2;
                layers.get(1)[t/2 + 2][t - 1] = o2;
                layers.get(1)[t - 2][t - 3] = rock3;
                //hole
                layers.get(1)[t/2 + 4][t/2] = hole1;
                //collectible
                layers.get(1)[t/2][t/2 + 2] = new Collectible(this, TYPE_STRING);
                //level exit
                layers.get(1)[t - 4][t - 1] = exit;
                //trees
                if (treesOn) {
                    StaticObject o0 = new StaticObject(this, TYPE_TREE);
                    layers.get(1)[t-5][t/2 + 2] = o0;
                    layers.get(1)[t - 4][t - 3] = o0;
                }
                break;

            case 2:
                // level 3

                //horizontal model
                createBackground(0, t/2, t/2, t, TYPE_GRASS_3, TYPE_DIRT_3);
                //wall of rocks
                StaticObject o3 = new StaticObject(this, TYPE_ROCK_3);
                for (int j = t/2 - 2; j < t/2; j++) layers.get(1)[t/2 + 4][j] = o3; // horizontal
                for (int j = t/2 - 2; j < t/2; j++) layers.get(1)[t/2 + 2][j] = o3; // horizontal
                for (int i = t/2; i < t; i++) layers.get(1)[i][t/2 - 3] = o3; // vertical
                layers.get(1)[t/2 + 1][t/2 - 1] = o3;
                //collectible
                c = new Collectible(this, TYPE_OBJ1);
                layers.get(1)[t/2][t/2 - 2] = c;
                //rock
                StaticObject rock5 = new StaticObject(this, TYPE_ROCK_3);
                StaticObject rock6 = new StaticObject(this, TYPE_ROCK_3);
                layers.get(1)[t/2 + 3][t/2 - 3] = rock5;
                layers.get(1)[t/2][t/2 - 1] = rock6;
                //leader
                layers.get(1)[t - 3][3] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, new Collectible(this, TYPE_FLOWER),rock5);
                layers.get(1)[t - 3][4] = new StaticObject(this, TYPE_CHAR1);

                //vertical model
                createBackground(t/2, 0, t, t/2, TYPE_GRASS_3, TYPE_DIRT_3);
                //walls of rocks
                for (int j = t/2 + 3; j < t - 3; j++) layers.get(1)[1][j] = o3; // horizontal
                for (int i = 3; i < t/2; i++) layers.get(1)[i][t/2 + 1] = o3; // vertical
                for (int i = 1; i < 4; i++) layers.get(1)[i][t/2 + 2] = o3; // vertical
                for (int i = 1; i < 6; i++) layers.get(1)[i][t - 3] = o3; // vertical
                for (int i = t/2 - 4; i < t/2; i++) layers.get(1)[i][t/2 + 3] = o3; // vertical
                for (int i = t/2 - 4; i < t/2; i++) layers.get(1)[i][t/2 + 5] = o3; // vertical
                layers.get(1)[t/2 - 2][t/2 + 4] = o3;
                //rocks to be triggered
                StaticObject rock7 = new StaticObject(this, TYPE_ROCK_3);
                layers.get(1)[t/2 - 5][t/2 + 4] = rock7;
                StaticObject rock8 = new StaticObject(this, TYPE_ROCK_3);
                layers.get(1)[0][t - 3] = rock8;
                //switch
                layers.get(1)[0][t/2] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, new Collectible(this, TYPE_STRING),rock6);
                layers.get(1)[3][t/2 + 4] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, new Collectible(this, TYPE_WIDGET),rock7);
                //level exit
                layers.get(1)[t/2 - 4][t/2 + 4] = exit;

                //transition model
                createBackground(t / 2, t / 2, t, t, TYPE_GRASS_3, TYPE_DIRT_3);
                //objects
                //walls of rocks
                for (int j = t/2; j < t/2 + 6; j++) layers.get(1)[t/2 + 2][j] = o3;// horizontal
                for (int j = t - 3; j < t; j++) layers.get(1)[t - 4][j] = o3; // horizontal
                for (int i = t/2; i < t/2 + 3; i++) layers.get(1)[i][t - 4] = o3; // vertical
                for (int i = t - 5; i < t - 2; i++) layers.get(1)[i][t/2 + 3] = o3; // vertical
                layers.get(1)[t/2][t/2 + 1] = o3;
                layers.get(1)[t/2 + 4][t/2] = o3;
                //switch
                layers.get(1)[t - 3][t/2] = new ConditionalSwitch(this, TYPE_SWITCH_OFF, new Collectible(this, TYPE_OBJ1),rock8);
                //rock to be triggered
                StaticObject rock9 = new StaticObject(this, TYPE_ROCK_3);
                layers.get(1)[t/2 + 2][t - 5] = rock9;
                //lever
                layers.get(1)[t - 2][t - 3] = new Lever(rock9);

                break;

        } //end of SWITCH statement
    }

    /**
     * Creates background objects. Takes matrix coordinates.
     * @param startX - start horizontal
     * @param startY - start vertical
     * @param endX - end horizontal
     * @param endY - end orizontal
     */
    void createBackground(int startX, int startY, int endX, int endY, int sprite1, int sprite2) {
        Random r = new Random();
        GameObject o1 = new StaticObject(this, sprite1);
        GameObject o2 = new StaticObject(this, sprite2);
        for (int i = startY; i < endY; i++)
            for (int j = startX; j < endX; j++) {
                layers.get(0)[i][j] = o1;
                if (r.nextInt(t) < i)
                    layers.get(0)[i][j] = o2;
            }
    }

    /**
     * Constructs a level grid.
     * @param view - view object
     * @param level - level index
     */
    void constructGrid(PuzzleView view, int level) {
        layers = new ArrayList<GameObject[][]>(NO_LAYERS);
        GameObject[][] background = new GameObject[t][t];
        GameObject[][] obj = new GameObject[t][t];
        layers.add(background);
        layers.add(obj);
        createObjects(view, level);
    }

    /**
     * Creates a navigation mesh for the current layout.
     */
    public void createNavMesh() {
        nm = new NavMesh();
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < t; j++) {
                //check (i, j) possible node
                if (checkClear(i, j)) {
                    Node n;

                    if (!nm.findNode(j, i)) { // check if node is not already added
                        n = new Node(j, i);
                        nm.addNode(n);
                    } else {
                        n = nm.getNode(j, i);
                    }

                    //add neighbours top, down, left, right
                    if (i-1 > 0)
                        if (checkClear(i - 1, j)) {
                            if (nm.findNode(j, i-1)) {
                                Node r = nm.getNode(j, i - 1);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j, i - 1);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (i+1 < t)
                        if (checkClear(i + 1, j)) {
                            if (nm.findNode(j, i + 1)) {
                                Node r = nm.getNode(j, i + 1);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j, i + 1);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (j-1 >= 0)
                        if (checkClear(i, j - 1)) {
                            if (nm.findNode(j - 1, i)) {
                                Node r = nm.getNode(j - 1, i);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j - 1, i);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                    if (j+1 < t)
                        if (checkClear(i, j + 1)) {
                            if (nm.findNode(j + 1, i)) {
                                Node r = nm.getNode(j + 1, i);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                            else {
                                Node r = new Node(j + 1, i);
                                nm.addNode(r);
                                if (!n.neighbours.contains(r))
                                    n.addNeighbour(r);
                                if (!r.neighbours.contains(n))
                                    r.addNeighbour(n);
                            }
                        }
                }
            }
        }
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BACKGROUND-----\n");
        for (int i = 0; i<t; i++) {
            sb.append(Arrays.toString(layers.get(0)[i])).append("\n");
        }
        sb.append("---------------------\n");
        sb.append("-----OBJECTS-----\n");
        for (int i = 0; i < t; i++) {
            for (int j = 0; j < t; j++) {
                if (layers.get(1)[i][j] != null)
                    sb.append(layers.get(1)[i][j]).append("\n");
            }
        }
        sb.append("---------------------\n");
        return sb.toString();}
}