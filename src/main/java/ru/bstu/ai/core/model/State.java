package ru.bstu.ai.core.model;

import com.google.gson.JsonArray;
import kotlin.Pair;
import ru.bstu.ai.core.enums.Move;
import ru.bstu.ai.core.enums.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Описываем тут состояние короче игры чо по чем
 */
public class State {

    private Cupboard cupboard;
    private final Field field;
    private final State prevState;
    private double value;
    private double bestChildValue = 99999999999999.;
    private boolean isValid = true;

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

    public State(String filename) {
        this.field = new Field();
        this.prevState = null;

        List<String> str = null;
        try {
            str = Files.readAllLines(Path.of(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.field.n = str.size() - 2;
        this.field.m = str.get(0).length();
        this.field.map = new int[this.field.m][];
        for(int i = 0; i < this.field.n; i++){
            this.field.map[i] = new int[this.field.m];
        }

        for (int i = 0; i < this.field.n; i++) {
            for (int j = 0; j < this.field.m; j++) {
                this.field.map[i][j] = Integer.parseInt(String.valueOf(str.get(i).charAt(j)));
            }
        }

        String stringWinCup = str.get(str.size() - 2);
        String[] split = stringWinCup.split(",");
        Position position;

        switch(split[2]) {
            case "1": position = Position.SQUARE; break;
            case "2": position = Position.VERTICAL; break;
            case "3": position = Position.HORIZONTAL; break;
            default: throw new IllegalArgumentException("Seriously?!");
        }

        this.field.winCup = new Cupboard(Integer.parseInt(split[0]),Integer.parseInt(split[1]),position);

        String stringCup = str.get(str.size() - 1);
        String[] split2 = stringCup.split(",");
        Position position2;

        switch(split2[2]) {
            case "1": position2 = Position.SQUARE; break;
            case "2": position2 = Position.VERTICAL; break;
            case "3": position2 = Position.HORIZONTAL; break;
            default: throw new IllegalArgumentException("Seriously?!");
        }

        this.cupboard = new Cupboard(Integer.parseInt(split2[0]),Integer.parseInt(split2[1]), position2);
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
                        field.isProbablePoint(cupboard.getX() + 1, cupboard.getY() + 1);
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

    public JsonArray toJson() {
        JsonArray jsonElementsMain = new JsonArray();
        List<Pair<Integer, Integer>> pointsCup = cupboard.getPoints();
        List<Pair<Integer, Integer>> pointsWin = field.winCup.getPoints();

        for (int i = 0; i < field.map.length; i++) {
            JsonArray jsonElements = new JsonArray();
            jsonElementsMain.add(jsonElements);
            for (int j = 0; j < field.map[i].length; j++) {
                int finalI = i;
                int finalJ = j;
                if (pointsCup.stream().anyMatch(p -> p.getFirst().equals(finalJ) && p.getSecond().equals(finalI))) {
                    jsonElements.add(2);
                }
                else if (pointsWin.stream().anyMatch(p -> p.getFirst().equals(finalJ) && p.getSecond().equals(finalI))) {
                    jsonElements.add(5);
                } else {
                    jsonElements.add(field.map[i][j]);
                }
            }
        }
        return jsonElementsMain;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        System.out.println("new value: " + value + getSizeHistory());
        this.value = value + getSizeHistory();
    }

    public void setSimpleValue(double value) {
        System.out.println("new value: " + value);
        this.value = value;
    }

    public void setBestChildValue(double bestChildValue) {
        this.bestChildValue = Math.min(bestChildValue, this.bestChildValue);
    }

    public double getBestChildValue() {
        return bestChildValue;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getSizeHistory() {
        int sumSteps = 0;
        State cur = this;
        while (true) {
            State prev = cur.prevState;
            if (prev != null) {
                sumSteps += Math.max(
                        Math.abs(prev.getCupboard().getX() - cur.getCupboard().getX()),
                        Math.abs(prev.getCupboard().getY() - cur.getCupboard().getY()
                        )
                );
                cur = prev;
            } else {
                break;
            }
        }
        return sumSteps;
    }

    public void deleteBestChildValue() {
        bestChildValue = 99999999999999.;
    }
}
