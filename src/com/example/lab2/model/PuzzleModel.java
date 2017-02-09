package com.example.lab2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.example.lab2.Constants.*;

/**
 * Created by Raluca on 16-Feb-16.
 */

public class PuzzleModel implements Serializable {
    public ArrayList<GameObject[][]> layers; // list of all layers in the game - 2x2 grids
    public int n;
    public int m;
    int t; // no of rows & columns + total square size
    boolean solved;
    Random r = new Random();

    public PuzzleModel(int o, int t) {
        this.t = t;
        reset(o);
    }

    void isSolved () {
        solved = false;
    }

    void reset (int o) { // orientation: 0 - horizontal, 1 - vertical
        if (o == 0) {
            this.n = t/2;
            this.m = t;
        } else {
            this.n = t;
            this.m = t/2;
        }
        constructGrid(n, m, t);
        solved = false;
    }

    void constructGrid(int n, int m, int t) {
        layers = new ArrayList<GameObject[][]>(NO_LAYERS);
        GameObject[][] background = new GameObject[n][m];
        GameObject[][] obj = new GameObject[n][m];
        layers.add(background);
        layers.add(obj);
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("-----BACKGROUND-----\n");
        for (int i = 0; i<n; i++) {
            sb.append(Arrays.toString(layers.get(0)[i])).append("\n");
        }
        sb.append("---------------------\n");
        sb.append("-----OBJECTS-----\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (layers.get(1)[i][j] != null)
                    sb.append(layers.get(1)[i][j]).append("\n");
            }
        }
        sb.append("---------------------\n");
        return sb.toString();
    }
}