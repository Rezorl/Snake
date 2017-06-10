package com.example.dawid.snake.location;

/**
 * Created by Dawid on 15.05.2017.
 */

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y)
    {
        this.x=x;
        this.y=y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }
}
