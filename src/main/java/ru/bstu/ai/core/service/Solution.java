package ru.bstu.ai.core.service;

import ru.bstu.ai.core.model.State;
import ru.bstu.ai.core.model.Statistic;

import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public interface Solution {

    /**
     * Решение нашего шкафичка
     * @param initState начальное состояние нашей карты
     * @param callback для отображения в ui текущего состояния
     * @return результирующее состояние
     */
    Statistic solve(State initState);




}
