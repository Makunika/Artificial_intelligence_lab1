package ru.bstu.ai.core.service;

import ru.bstu.ai.core.model.Move;
import ru.bstu.ai.core.model.State;
import ru.bstu.ai.core.model.Statistic;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * логика ИИ с поиском в ШИРИНУ
 */
public class SolutionWideImpl implements Solution {

    @Override
    public Statistic solve(State initState) {
        Queue<State> arrO = new LinkedList<>();
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

            System.out.println(x);
            System.out.println("=======================!!!!!===");
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
            if (!(arrO.contains(newState) || arrC.contains(newState))) {
                arrO.offer(newState);
            }
        }
        if (x.isProbableMove(Move.LEFT)) {
            State newState = x.moveAndGetNewState(Move.LEFT);
            if (!(arrO.contains(newState) || arrC.contains(newState))) {
                arrO.offer(newState);
            }
        }
        if (x.isProbableMove(Move.RIGHT)) {
            State newState = x.moveAndGetNewState(Move.RIGHT);
            if (!(arrO.contains(newState) || arrC.contains(newState))) {
                arrO.offer(newState);
            }
        }
        if (x.isProbableMove(Move.DOWN)) {
            State newState = x.moveAndGetNewState(Move.DOWN);
            if (!(arrO.contains(newState) || arrC.contains(newState))) {
                arrO.offer(newState);
            }
        }
    }
}
