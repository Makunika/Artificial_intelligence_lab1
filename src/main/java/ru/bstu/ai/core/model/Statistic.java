package ru.bstu.ai.core.model;

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
