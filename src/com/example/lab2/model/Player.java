package com.example.lab2.model;

import com.example.lab2.*;
import com.example.lab2.model.navigation.NavMesh;
import com.example.lab2.model.navigation.Node;
import static com.example.lab2.Constants.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Raluca on 07-Mar-16.
 */
public class Player extends GameObject implements Serializable {
    NavMesh nm;
    ArrayList<Node> path;
    boolean moving;
    Thread move;
    Storage inventory;
    static final int SLEEP_TIME = 200;
    static final String tag = "done; iterations: ";
    static final String tagP = ":Player ", tagI = "index";

    public Player (Vector2D pos, CornerPuzzleModel model) {
        this.pos = new Vector2D(pos);
        game = model;
        moving = false;
        move = null;
        inventory = new Storage(TYPE_BACKPACK, 4);
    }

    public boolean addItem (Collectible c) { return inventory.addItem(c); }

    public Collectible getItem (String p, int id) {
        return inventory.getItem(p, id);
    }

    public ArrayList<Collectible> getItems () { return inventory.getObjects();}

    public boolean removeItem (String p, int id) {
        return inventory.removeItem(p, id);
    }

    public boolean removeItem (Collectible c) {
        return inventory.removeItem(c);
    }

    public void setPos(Vector2D to, PuzzleView view, NavMesh n) {
        if (game.checkClear(to.x, to.y)) {
            nm = n;
            // calculate path from current position to destination position
            path = nm.getRoute(nm.getNode(pos.x, pos.y), nm.getNode(to.x, to.y));
            moving = true;
            //spawn new thread to move there
            if (move != null) {
                move.interrupt();
            }
            move = new Thread(new MoveRunnable(view));
            move.start();
        }
    }

    public void interrupt() {
        move.interrupt();
    }

    @Override
    public boolean execute(PuzzleView view) {
        //remove last item in inventory
        return removeItem(tagI, inventory.numItems() - 1);
    }

    class MoveRunnable extends Thread {
        PuzzleView view;

        MoveRunnable (PuzzleView view) {this.view = view;}

        @Override
        public void run() {
            //move along the path
            try {
                int count = 0;
                while (!path.isEmpty()) {
                    count++;
                    Node n = path.get(0);
                    pos.x = n.x;
                    pos.y = n.y;
                    path.remove(n);
                    view.postInvalidate();
                    Thread.sleep(SLEEP_TIME);
                }
                System.out.println(tag + count);
                this.interrupt();
                move = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString () {
        return id + tagP;
    }
}
