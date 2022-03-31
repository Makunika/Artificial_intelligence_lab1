package ru.bstu.ai.core.service;

import ru.bstu.ai.core.enums.Move;
import ru.bstu.ai.core.model.Cupboard;
import ru.bstu.ai.core.model.State;
import ru.bstu.ai.core.model.Statistic;

import java.util.*;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SolutionASearchImpl implements Solution {

    @Override
    public Statistic solve(State initState) {
        PriorityQueue<State> arrO = new PriorityQueue<>(Comparator.comparingDouble(State::getValue));

        arrO.offer(initState);
        PriorityQueue<State> arrC = new PriorityQueue<>(Comparator.comparingDouble(State::getBestChildValue));

        int iteration = 0;
        int maxO = 0;

        while (!arrO.isEmpty()) {
            if (arrO.size() > maxO) {
                maxO = arrO.size();
            }
            iteration++;

            State x = arrO.poll();
            if (x.isWin()) {
                return new Statistic(
                        iteration,
                        maxO,
                        arrO.size(),
                        arrC.size(),
                        x
                );
            }
            arrC.add(x);
            p(arrO, arrC, x);
            afterIteration(arrO, arrC, x);
        }
        return new Statistic(
                iteration,
                maxO,
                0,
                arrC.size(),
                null
        );
    }

    protected void afterIteration(PriorityQueue<State> arrO, PriorityQueue<State> arrC, State x) {

    }

    protected void p(Queue<State> arrO, Queue<State> arrC, State x) {
        if (x.isProbableMove(Move.UP)) {
            State newState = x.moveAndGetNewState(Move.UP);
            newState.setValue(getHeuristics().stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.LEFT)) {
            State newState = x.moveAndGetNewState(Move.LEFT);
            newState.setValue(getHeuristics().stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.RIGHT)) {
            State newState = x.moveAndGetNewState(Move.RIGHT);
            newState.setValue(getHeuristics().stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.DOWN)) {
            State newState = x.moveAndGetNewState(Move.DOWN);
            newState.setValue(getHeuristics().stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
    }

    private void processNewState(Queue<State> arrO, Queue<State> arrC, State prevState, State newState) {
        if (!(arrO.contains(newState) || arrC.contains(newState))) {
            arrO.offer(newState);
        } else if (arrO.contains(newState) && newState.getValue() < prevState.getValue()) {
            prevState.setSimpleValue(newState.getValue());
        } else if (arrC.contains(newState) && newState.getValue() < prevState.getValue()) {
            prevState.setSimpleValue(newState.getValue());
            arrO.offer(newState);
            arrC.remove(newState);
        }
    }

    protected List<Function<State, Double>> getHeuristics() {
        return heuristics;
    }

    private final List<Function<State, Double>> heuristics = List.of(
            (state) -> {
                /*
                 * 1.5
                 * 11
                 *  |
                 *  |------&&
                 *
                 * */

                Cupboard winPoint = state.getField().winCup;
                Cupboard cup = state.getCupboard();

                int horizontal = Math.abs(winPoint.getX() - cup.getX());
                int vertical = Math.abs(winPoint.getY() - cup.getY());
                return (horizontal + vertical) / ((8. / 12. * 2.) + (8. / 4.));
            },
            (state) -> {
                double koeffWall = 1.5;

                Cupboard winPoint = state.getField().winCup;
                Cupboard cup = state.getCupboard();

                int horizontal = Math.abs(winPoint.getX() - cup.getX());
                int vertical = Math.abs(winPoint.getY() - cup.getY());

                if (horizontal + vertical < 20) {
                    int horizontalNoAbs = horizontal != 0 ? (winPoint.getX() - cup.getX()) / horizontal : 0;
                    int verticalNoAbs = vertical != 0 ? (winPoint.getY() - cup.getY()) / vertical : 0;

                    int xCurrent = cup.getX();
                    int yCurrent = cup.getY();
                    if (
                            checkWall(state, horizontal, vertical, horizontalNoAbs, verticalNoAbs, xCurrent, yCurrent)
                                    &&
                            checkWall(state, vertical, horizontal, verticalNoAbs, horizontalNoAbs, xCurrent, yCurrent)
                    ) {
                        return ((horizontal + vertical) / ((8. / 12. * 2.) + (8. / 4.))) * koeffWall;
                    }
                }

                return 0.;
            }
    );

    private boolean checkWall(State state, int pathOne, int pathTwo, int addPathOne, int addPathTwo, int xCurrent, int yCurrent) {
        for (int i = 0; i < pathOne; i++) {
            xCurrent += addPathOne;
            if (state.getField().isProbablePoint(xCurrent, yCurrent)) {
                return true;
            }
        }
        for (int i = 0; i < pathTwo; i++) {
            yCurrent += addPathTwo;
            if (state.getField().isProbablePoint(xCurrent, yCurrent)) {
                return true;
            }
        }
        return false;
    }
}
