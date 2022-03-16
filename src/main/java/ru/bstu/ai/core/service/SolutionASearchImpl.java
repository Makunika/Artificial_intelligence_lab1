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
        List<State> arrC = new LinkedList<>();

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
        }
        return new Statistic(
                iteration,
                maxO,
                0,
                arrC.size(),
                null
        );
    }

    private void p(Queue<State> arrO, List<State> arrC, State x) {
        if (x.isProbableMove(Move.UP)) {
            State newState = x.moveAndGetNewState(Move.UP);
            newState.setValue(heuristics.stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.LEFT)) {
            State newState = x.moveAndGetNewState(Move.LEFT);
            newState.setValue(heuristics.stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.RIGHT)) {
            State newState = x.moveAndGetNewState(Move.RIGHT);
            newState.setValue(heuristics.stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
        if (x.isProbableMove(Move.DOWN)) {
            State newState = x.moveAndGetNewState(Move.DOWN);
            newState.setValue(heuristics.stream().mapToDouble(f -> f.apply(newState)).max().orElse(0));
            processNewState(arrO, arrC, x, newState);
        }
    }

    private void processNewState(Queue<State> arrO, List<State> arrC, State prevState, State newState) {
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

    private final List<Function<State, Double>> heuristics = List.of(
            (state) -> {
                Cupboard winPoint = state.getField().winCup;
                State prevState = state.getPrevState();
                if (prevState == null) {
                    Cupboard newCup = state.getCupboard();
                    return sqrt(pow(winPoint.getX()-newCup.getX(),2) + pow(winPoint.getY()-newCup.getY(),2));
                }
                Cupboard previousCup = prevState.getCupboard();
                Cupboard newCup = state.getCupboard();
                double prevDif = sqrt(pow(winPoint.getX()-previousCup.getX(),2) + pow(winPoint.getY()-previousCup.getY(),2));
                double newDif = sqrt(pow(winPoint.getX()-newCup.getX(),2) + pow(winPoint.getY()-newCup.getY(),2));
                return Math.min(prevDif,newDif);
            }
//            ,
//            (state) -> {
//                Cupboard current = state.getCupboard();
//                Cupboard vertical = new Cupboard(state.getField().winCup.getX(),current.getY(),state.getField().winCup.getPosition());
//                Cupboard horizontal = new Cupboard(current.getX(),state.getField().winCup.getY(),state.getField().winCup.getPosition());
//
//                double horValue = sqrt(pow(horizontal.getX()-current.getX(),2) + pow(horizontal.getY()-current.getY(),2));
//                double vertValue = sqrt(pow(vertical.getX()-current.getX(),2) + pow(vertical.getY()-current.getY(),2));
//                return Math.min(vertValue,horValue);
//            }
    );
}
