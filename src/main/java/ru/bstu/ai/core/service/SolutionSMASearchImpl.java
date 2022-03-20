package ru.bstu.ai.core.service;

import ru.bstu.ai.core.model.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class SolutionSMASearchImpl extends SolutionASearchImpl {

    @Override
    protected void afterIteration(PriorityQueue<State> arrO, PriorityQueue<State> arrC, State x) {
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
}
