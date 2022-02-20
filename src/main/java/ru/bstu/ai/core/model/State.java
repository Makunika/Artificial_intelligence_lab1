package ru.bstu.ai.core.model;

import kotlin.Pair;

import java.util.List;
import java.util.Objects;

/**
 * Описываем тут состояние короче игры чо по чем
 */
public class State {

    private Cupboard cupboard;
    private Field field;
    private State prevState;

    public State(Cupboard cupboard, Field field) {
        this.cupboard = cupboard;
        this.field = field;
        this.prevState = null;
    }

    public State(Cupboard cupboard, Field field, State prevState) {
        this.cupboard = cupboard;
        this.field = field;
        this.prevState = prevState;
    }


    /*
    VERTICAL:
    [*]
    [ ]
    HORIZONTAL:
    [*][ ]
    SQUARE:
    [*][ ]
    [ ][ ]
     */

    public boolean isProbableMove(Move move) {
        Cupboard cupboard = this.cupboard.moveAndNewGet(move);

        switch (cupboard.getPosition()) {
            case SQUARE:
                return
                        field.isProbablePoint(cupboard.getX(), cupboard.getY()) &&
                        field.isProbablePoint(cupboard.getX() , cupboard.getY() + 1) &&
                        field.isProbablePoint(cupboard.getX() + 1, cupboard.getY()) &&
                        field.isProbablePoint(cupboard.getX()+1, cupboard.getY() + 1);
            case VERTICAL:
                return
                        field.isProbablePoint(cupboard.getX(), cupboard.getY()) &&
                                field.isProbablePoint(cupboard.getX(), cupboard.getY() + 1);
            case HORIZONTAL:
                return
                        field.isProbablePoint(cupboard.getX(), cupboard.getY()) &&
                                field.isProbablePoint(cupboard.getX() + 1, cupboard.getY());
        }
        
        return false;
    }
    
    public State moveAndGetNewState(Move move) {
        Cupboard cupboard = this.cupboard.moveAndNewGet(move);
        return new State(cupboard, this.field, this);
    }

    public State move(Move move) {
        if (!isProbableMove(move)) {
            throw new RuntimeException("ты хуй");
        }
        this.cupboard = this.cupboard.moveAndNewGet(move);
        return this;
    }

    public boolean isWin() {
        return cupboard.equals(field.winCup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(cupboard, state.cupboard);
    }


    @Override
    public String toString() {
        
        /*
        1 1 1 ! !
        1 * * 1 1
        0 1 0 1 1
         */

        StringBuilder sb = new StringBuilder();

        List<Pair<Integer, Integer>> pointsCup = cupboard.getPoints();
        List<Pair<Integer, Integer>> pointsWin = field.winCup.getPoints();

        for (int i = 0; i < field.map.length; i++) {
            for (int j = 0; j < field.map[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                if (pointsCup.stream().anyMatch(p -> p.getFirst().equals(finalJ) && p.getSecond().equals(finalI))) {
                    sb.append("&");
                }
                else if (pointsWin.stream().anyMatch(p -> p.getFirst().equals(finalJ) && p.getSecond().equals(finalI))) {
                    sb.append("!");
                } else {
                    sb.append(field.map[i][j]);
                }
                sb.append(" ");
            }
            sb.append("\n");
        }

        sb.append("============================");

        return sb.toString();
    }

    public Cupboard getCupboard() {
        return cupboard;
    }

    public Field getField() {
        return field;
    }

    public State getPrevState() {
        return prevState;
    }
}
