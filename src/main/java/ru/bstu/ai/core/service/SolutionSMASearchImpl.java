package ru.bstu.ai.core.service;

import ru.bstu.ai.core.enums.Move;
import ru.bstu.ai.core.enums.Position;
import ru.bstu.ai.core.model.Cupboard;
import ru.bstu.ai.core.model.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SolutionSMASearchImpl extends SolutionASearchImpl {

//    int right = 20;
//    int left = 8;

    int right = 15;
    int left = 5;

    public SolutionSMASearchImpl() {
    }

    public SolutionSMASearchImpl(int right, int left) {
        this.right = right;
        this.left = left;
    }

    @Override
    protected void afterIteration(PriorityQueue<State> arrO, PriorityQueue<State> arrC, State x) {
        if (true) {
            return;
        }
        List<State> result = new ArrayList<>();
        while (!arrO.isEmpty() &&
                !arrC.isEmpty() &&
                arrC.peek().getBestChildValue() < 9999. &&
                arrO.peek().getValue() > arrC.peek().getBestChildValue()) {

            State toDelete = arrO.poll();
            State prevState = toDelete.getPrevState();
            if (prevState != null) {
                prevState.setBestChildValue(toDelete.getValue());
            }
            State poll = arrC.poll();
            poll.deleteBestChildValue();
            result.add(poll);
        }

        if (arrO.isEmpty() && result.isEmpty()) {
            arrO.addAll(arrC.stream().limit(20).collect(Collectors.toList()));
        }

        for (State state : result) {
            arrO.offer(state);
        }

        if (arrO.size() > 20) {
            arrO
                    .stream()
                    .max(Comparator.comparingDouble(State::getValue))
                    .ifPresent(toDelete -> {
                        State prevState = toDelete.getPrevState();
                        if (prevState != null) {
                            prevState.setBestChildValue(toDelete.getValue());
                        }
                        arrO.remove(toDelete);
                        arrC.remove(toDelete);
                    });
        }
    }


    @Override
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

                double horizontal = Math.abs(winPoint.getX() - cup.getX());
                double vertical = Math.abs(winPoint.getY() - cup.getY());
                return (horizontal + vertical) / ((8. / 12. * 2.) + (8. / 4.));
            },
            (state) -> {
                Cupboard winPoint = state.getField().winCup;
                Cupboard cup = state.getCupboard();

                double horizontal = Math.abs(winPoint.getX() - cup.getX());
                double vertical = Math.abs(winPoint.getY() - cup.getY());

                if (horizontal + vertical < right && horizontal + vertical > left) {
                    return getMinPath(state, 5);
                } else {
                    return 0.;
                }
            }
    );

    private double getMinPath(State state, int maxDeep) {
        Cupboard winPoint = state.getField().winCup;
        Cupboard cup = state.getCupboard();
        return minPathForWinCup(state, winPoint, cup, 1, maxDeep);
    }

    private double pathToCup(State state, Cupboard winCup, Cupboard cup, int stepForWin) {
        double path = 999999;
        if(state.getField().isProbableCup(winCup)){
            double horizontalTop = Math.abs(winCup.getX() - cup.getX());
            double verticalTop = Math.abs(winCup.getY() - cup.getY());
            path = (horizontalTop + verticalTop) / ((8. / 12. * 2.) + (8. / 4.)) + stepForWin;
        }
        return path;
    }

    private double minPathForWinCup(State state, Cupboard winCup, Cupboard cup, int step, int maxDeep) {
        if (maxDeep <= step) {
            return pathToCup(state, winCup, cup, step);
        }

        Cupboard winTop = winCup.moveAndNewGet(Move.UP);
        Cupboard winBottom = winCup.moveAndNewGet(Move.DOWN);
        Cupboard winRight = winCup.moveAndNewGet(Move.RIGHT);
        Cupboard winLeft = winCup.moveAndNewGet(Move.LEFT);

        return Stream.of(
                        winTop,
                        winBottom,
                        winRight,
                        winLeft
                )
                .filter(wc -> state.getField().isProbableCup(wc))
                .map(wc -> minPathForWinCup(state, winTop, cup, step + 1, maxDeep))
                .filter(path -> path.compareTo(0.) >= 0)
                .min(Double::compareTo)
                .orElse(-1.);
    }
}
