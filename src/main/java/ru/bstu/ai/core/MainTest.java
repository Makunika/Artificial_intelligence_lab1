package ru.bstu.ai.core;

import ru.bstu.ai.core.enums.Position;
import ru.bstu.ai.core.model.*;
import ru.bstu.ai.core.service.Solution;
import ru.bstu.ai.core.service.SolutionDepthImpl;
import ru.bstu.ai.core.service.SolutionSMASearchImpl;

public class MainTest {

    public static void main(String[] args) {

        Result bestIteration = new Result();
        Result bestShag = new Result();
        SolutionSMASearchImpl solutionSMASearch1 = new SolutionSMASearchImpl(15, 5);
        Statistic solve2 = solutionSMASearch1.solve(new State("file.txt"));
        solve2.printStatSmall();

        for (int left = 1; left < 25; left++) {
            for (int right = left; right < left + 15; right++) {
                System.out.println("left = " + left );
                System.out.println("right = " + right );
                SolutionSMASearchImpl solutionSMASearch = new SolutionSMASearchImpl(right, left);
                Statistic solve = solutionSMASearch.solve(new State("file.txt"));
                solve.printStatSmall();
                if (bestIteration.getStatistic() == null || bestIteration.getStatistic().getCountIteration() > solve.getCountIteration()) {
                    bestIteration.setStatistic(solve);
                    bestIteration.setLeft(left);
                    bestIteration.setRight(right);
                }
                if (bestShag.getStatistic() == null || bestShag.getStatistic().getEndState().getCountSteps() > solve.getEndState().getCountSteps()) {
                    bestShag.setStatistic(solve);
                    bestShag.setLeft(left);
                    bestShag.setRight(right);
                }
            }
        }
        System.out.println(bestIteration);
        System.out.println("ШАГИ");
        System.out.println(bestShag);
    }


    static class Result {
        Statistic statistic;
        int left;
        int right;

        public Result() {}

        public Statistic getStatistic() {
            return statistic;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        public void setStatistic(Statistic statistic) {
            this.statistic = statistic;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public void setRight(int right) {
            this.right = right;
        }

        @Override
        public String toString() {
            statistic.printStatSmall();
            return "Result{" +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
