package ru.bstu.ai.core.service;

import ru.bstu.ai.core.model.Cupboard;
import ru.bstu.ai.core.model.State;

import java.util.List;
import java.util.function.Function;

public class SolutionABeforeSearhImpl extends SolutionASearchImpl {

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

                int horizontal = Math.abs(winPoint.getX() - cup.getX());
                int vertical = Math.abs(winPoint.getY() - cup.getY());
                return (horizontal + vertical) / ((8. / 12. * 2.) + (8. / 4.));
            }
    );
}
