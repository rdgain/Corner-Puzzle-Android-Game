package com.example.lab2.model;

import java.io.Serializable;

import static java.lang.Math.*;

// mutable 2D vectors
public final class Vector2D implements Serializable {

    // fields
    public int x, y;

    // construct a null vector
    public Vector2D(){
        x = 0;
        y = 0;
    }

    // construct a vector with given coordinates
    public Vector2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    // construct a vector that is a copy of the argument
    public Vector2D(Vector2D v){
        this.x = v.x;
        this.y = v.y;
    }

    // average 2 vectors that is a copy of the argument
    public static Vector2D average (Vector2D v1, Vector2D v2){
        return new Vector2D((v1.x + v2.x) / 2, (v1.y + v2.y) / 2);
    }

    // set coordinates
    public void set (int x, int y) {
        this.x = x;
        this.y = y;
    }

    // set coordinates to argument vector coordinates
    public void set (Vector2D v) {
        this.x = v.x;
        this.y = v.y;
    }

    // compare for equality (needs to allow for Object type argument...)
    public boolean equals(Object o) {
        return this.x == ((Vector2D)o).x && this.y == ((Vector2D)o).y;
    }

    //  magnitude (= "length") of this vector
    public double mag() {
        return hypot(x,y);
    }

    // angle between vector and horizontal axis in radians
    public double theta() {
        return atan2(y,x);
    }

    // String for displaying vector as text
    public String toString() {
        return "x: " + x + "; y: " + y + "; magnitude: " + this.mag();
    }

    // add argument vector
    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    // add coordinate values
    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    // weighted add - frequently useful
    public void add(Vector2D v, int fac) {
        this.x += fac * v.x;
        this.y += fac * v.y;
    }

    // multiply with factor
    public void mult(double fac) {
        this.x *= fac;
        this.y *= fac;
    }

    // "wrap" vector with respect to given positive values w and h
    // method assumes that x >= -w and y >= -h
    public void wrap(int w, int h) {
        if ( x <= 0)
            x = w-1;
        if ( y <= 0)
            y = h-1;
        x %= w;
        y %= h;
    }

    // rotate by angle given in radians
    public void rotate(double theta) {
        this.x = (int) (x*cos(theta) - y*sin(theta));
        this.y = (int) (x*sin(theta) + y*cos(theta));
    }

    // scalar product with argument vector
    public double scalarProduct(Vector2D v) {
        return this.x * v.x + this.y * v.y;
    }

    // distance to argument vector
    public double dist(Vector2D v) {
        return hypot(v.x - this.x, v.y - this.y);
    }

    // normalise vector so that mag becomes 1
    // direction is unchanged
    public void normalise() {
        if (mag() != 0) {
            double mag = mag();
            x = (int) (x / mag);
            y = (int) (y / mag);
        }
    }

    public static Vector2D minus(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.x - v2.x, v1.y - v2.y);
    }


    public Vector2D rotate90degreesAnticlockwise() {
        // Note: this is meant to create a new vector and not modify the current vector
        return new Vector2D(-y,x);
    }
}