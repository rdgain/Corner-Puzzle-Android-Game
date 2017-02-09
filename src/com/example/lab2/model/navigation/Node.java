package com.example.lab2.model.navigation;

import java.io.Serializable;
import java.util.ArrayList;

public class Node implements Serializable {

    public ArrayList<Node> neighbours;
    ArrayList<Node> route;
    boolean visited;
    public int x;
    public int y;
    int cost;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        cost = 1;
        visited = false;
        neighbours = new ArrayList<Node>();
        route = new ArrayList<Node>();
    }

    public void addNeighbour(Node n) {
        neighbours.add(n);
    }

    @Override
    public boolean equals (Object o) {
        return (this.x==((Node)o).x && this.y == ((Node)o).y);
    }

    public void removeNeighbour(Node n) {
        neighbours.remove(n);
    }

}
