package ru.bstu.ai.core.service;

import ru.bstu.ai.core.enums.Move;
import ru.bstu.ai.core.model.Cupboard;
import ru.bstu.ai.core.model.State;
import ru.bstu.ai.core.model.Statistic;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class SolutionSMASearchImpl implements Solution {

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

                List<State> collect = arrC.stream().filter(state -> state.isValid() && state.getBestChildValue() < 9999.)
                        .sorted(Comparator.comparingDouble(State::getBestChildValue))
                        .limit(20)
                        .collect(Collectors.toList());
                if (collect.stream().min(Comparator.comparingDouble(State::getBestChildValue))
                        .map(State::getBestChildValue).orElse(99999.) < x.getValue()) {
                    collect.forEach(state -> state.setValid(false));
                    arrO.clear();
                    arrO.addAll(collect);
                    continue;
                }

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

    private void p(Queue<State> arrO, Queue<State> arrC, State x) {
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
        if (arrO.size() > 20) {
            arrO
                    .stream()
                    .max(Comparator.comparingDouble(State::getValue))
                    .ifPresent(stateToDelete -> {
                        State prev = stateToDelete.getPrevState();
                        if (prev != null) {
                            prev.setBestChildValue(stateToDelete.getValue());
                        }
                        arrO.remove(stateToDelete);
                        arrC.remove(stateToDelete);
                    });
        }
        if (arrO.isEmpty()) {
            List<State> collect = arrC.stream().filter(state -> state.isValid() && state.getBestChildValue() < 9999.)
                    .sorted(Comparator.comparingDouble(State::getBestChildValue))
                    .limit(20)
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                collect.forEach(state -> state.setValid(false));
                arrO.addAll(collect);
            }
        }
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

                double horizontal = Math.abs(winPoint.getX() - cup.getX());
                double vertical = Math.abs(winPoint.getY() - cup.getY());
                return (horizontal + vertical)*1.5;
            }
    );
}
