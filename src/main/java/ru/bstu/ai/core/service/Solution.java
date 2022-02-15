package ru.bstu.ai.core.service;

import ru.bstu.ai.core.model.State;

import java.util.function.Consumer;

public interface Solution {

    /**
     * Решение нашего шкафичка
     * @param initState начальное состояние нашей карты
     * @param callback для отображения в ui текущего состояния
     * @return результирующее состояние
     */
    State solve(State initState, Consumer<State> callback);

}
