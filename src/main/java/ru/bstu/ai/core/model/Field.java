package ru.bstu.ai.core.model;

import ru.bstu.ai.core.enums.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Field {

    public int[][] map = {
            {0,0,1,1,0},
            {1,1,1,1,1},
            {0,0,1,1,0},
            {1,0,1,1,1},
            {1,0,1,1,1}
    };
    public int m = 5;
    public int n = 5;
    public Cupboard winCup = new Cupboard(2,2, Position.SQUARE);

    //1 1 1 1 0
    //& & 1 1 1
    //0 1 ! ! 0
    //1 1 ! ! 1
    //1 1 1 1 1


    public Field(){

    }

    public Field(String filePath){
        List<String> str = null;
        try {
            str = Files.readAllLines(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        n = str.size() - 1;
        m = str.get(0).length();
        map = new int[m][];
        for(int i = 0; i < n; i++){
            map[i] = new int[m];
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                    map[i][j] = Integer.parseInt(String.valueOf(str.get(i).charAt(j)));
            }
        }

        String stringWinCup = str.get(str.size() - 1);
        String[] split = stringWinCup.split(",");
        Position position;

        switch(split[2]) {
            case "1": position = Position.SQUARE; break;
            case "2": position = Position.VERTICAL; break;
            case "3": position = Position.HORIZONTAL; break;
            default: throw new IllegalArgumentException("Seriously?!");
        }

        this.winCup = new Cupboard(Integer.parseInt(split[0]),Integer.parseInt(split[1]),position);

    }

    public Field(Integer[][] tam , Cupboard winSpot){
        n = tam.length;
        m = tam[0].length;
        map = new int[m][];
        for(int i = 0; i < n; i++){
            map[i] = new int[m];
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                map[i][j]=tam[i][j];
            }
        }
        winCup = winSpot;
    }


    //1 1 1 1 0 0
    //0 0 1 1 1 1
    //0 1 1 1 1 1
    //
    //
    //

    public boolean isProbablePoint(int x, int y) {
        try {
            return map[y][x] == 1;
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int[] integers : map) {
            for (Integer integer : integers) {
                sb.append(integer).append(" ");
            }
            sb.append("\n");
        }
        sb.append("winX = ").append(winCup.getX());
        sb.append("winY = ").append(winCup.getY());
        sb.append("position = ").append(winCup.getPosition());
        return sb.toString();
    }

    public double maxDiagonal() {
        return Math.sqrt(Math.pow(m, 2) +  Math.pow(n, 2));
    }
}
