package ru.bstu.ai.core.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Statistic {
    private int countIteration;
    private int maxO;
    private int endO;
    private int endC;

    private State endState;

    public Statistic(int countIteration, int maxO, int endO, int endC, State endState) {
        this.countIteration = countIteration;
        this.maxO = maxO;
        this.endO = endO;
        this.endC = endC;
        this.endState = endState;
    }

    public int getCountIteration() {
        return countIteration;
    }

    public void setCountIteration(int countIteration) {
        this.countIteration = countIteration;
    }

    public int getMaxO() {
        return maxO;
    }

    public void setMaxO(int maxO) {
        this.maxO = maxO;
    }

    public int getEndO() {
        return endO;
    }

    public void setEndO(int endO) {
        this.endO = endO;
    }

    public int getEndC() {
        return endC;
    }

    public void setEndC(int endC) {
        this.endC = endC;
    }

    public State getEndState() {
        return endState;
    }

    public void setEndState(State endState) {
        this.endState = endState;
    }

    public String toJson(){
        Gson gson = new GsonBuilder().create();
        if (endState == null) {
            return gson.toJson(null);
            //return "Решение не было найдено";
        }
        JsonArray jsonElements = new JsonArray();
        State current = endState;
        while (current != null) {
            jsonElements.add(current.toJson());
            current = current.getPrevState();
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("states", jsonElements);
        jsonObject.add("countIteration", gson.toJsonTree(countIteration));
        jsonObject.add("maxO", gson.toJsonTree(maxO));
        jsonObject.add("endO", gson.toJsonTree(endO));
        jsonObject.add("endC", gson.toJsonTree(endC));
        jsonObject.add("m", gson.toJsonTree(endState.getField().m));
        jsonObject.add("n", gson.toJsonTree(endState.getField().n));
        String json = gson.toJson(jsonObject);
        System.out.println(json);
        return json;
    }

    public void printStat() {
        System.out.println("Число итераций: " + countIteration);
        System.out.println("Максимальное число вершин-кандидатов: " + maxO);
        System.out.println("Число вершин-кандидатов на последней итерации: " + endO);
        System.out.println("Число пройденных вершин на последней итерации: " + endC);
        System.out.println("Путь:");
        if (endState == null) {
            System.out.println("Решение не было найдено");
        }
        State current = endState;
        while (current != null) {
            System.out.println(current);
            current = current.getPrevState();
        }
    }
}
