package ru.bstu.ai.core.model;

import kotlin.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cupboard {
    private int x;
    private int y;

    private Position position;

    public Cupboard(int x, int y, Position position) {
        this.x = x;
        this.y = y;
        this.position = position;
    }

    private void moveRight() {
        switch (position) {
            case SQUARE:
                position = Position.VERTICAL;
                x++;
                break;
            case VERTICAL:
                position = Position.SQUARE;
                break;
            case HORIZONTAL:
                x++;
                break;
        }
    }

    private void moveLeft() {
        switch (position) {
            case SQUARE:
                position = Position.VERTICAL;
                break;
            case VERTICAL:
                position = Position.SQUARE;
                x--;
                break;
            case HORIZONTAL:
                x--;
                break;
        }
    }

    private void moveUp() {
        switch (position) {
            case SQUARE:
                position = Position.HORIZONTAL;
                break;
            case VERTICAL:
                y--;
                break;
            case HORIZONTAL:
                position = Position.SQUARE;
                y--;
                break;
        }
    }

    private void moveDown() {
        switch (position) {
            case SQUARE:
                position = Position.HORIZONTAL;
                y++;
                break;
            case VERTICAL:
                y++;
                break;
            case HORIZONTAL:
                position = Position.SQUARE;
                break;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cupboard cupboard = (Cupboard) o;
        return x == cupboard.x && y == cupboard.y && position == cupboard.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, position);
    }

    public List<Pair<Integer, Integer>> getPoints() {
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        switch (position) {
            case SQUARE:
                result.add(new Pair<>(x, y));
                result.add(new Pair<>(x + 1, y + 1));
                result.add(new Pair<>(x, y + 1));
                result.add(new Pair<>(x + 1, y));
                break;
            case VERTICAL:
                result.add(new Pair<>(x, y));
                result.add(new Pair<>(x, y + 1));
                break;
            case HORIZONTAL:
                result.add(new Pair<>(x, y));
                result.add(new Pair<>(x + 1, y));
                break;
        }
        return result;
    }

    public Cupboard moveAndNewGet(Move move) {
        Cupboard newCupboard = new Cupboard(
                this.x,
                this.y,
                this.position
        );
        switch (move) {
            case RIGHT:
                newCupboard.moveRight();
                break;
            case LEFT:
                newCupboard.moveLeft();
                break;
            case UP:
                newCupboard.moveUp();
                break;
            case DOWN:
                newCupboard.moveDown();
                break;
        }
        return newCupboard;
    }
}
