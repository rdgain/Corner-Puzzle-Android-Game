package com.example.lab2.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Raluca on 21-Mar-16.
 */
public class Storage extends GameObject implements Serializable {
    int maxCapacity;
    private ArrayList<Collectible> objects;

    public Storage (int id, int maxCapacity) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        objects = new ArrayList<Collectible>();
    }

    public boolean addItem (Collectible c) {
        if (objects.size() >= maxCapacity) return false;
        else return objects.add(c);
    }

    public Collectible getItem (String p, int id) {
        if (p.equals("index"))
            return objects.get(id);
        else if (p.equals("id"))
            for (Collectible c: objects)
                if (c.id == id)
                    return c;
        return null;
    }

    public ArrayList<Collectible> getObjects () { return objects; }

    public boolean removeItem (String p, int id) {
        if (p.equals("index")) {
            return objects.remove(id) != null;
        }
        else if (p.equals("id"))
            for (Collectible c: objects)
                if (c.id == id)
                    return objects.remove(c);
        return false;
    }

    public boolean removeItem (Collectible c) {
        for (Collectible col : objects) {
            if (col.id == c.id)
                return objects.remove(col);
        }
        return objects.remove(c);
    }

    public int numItems() {
        return objects.size();
    }

}
