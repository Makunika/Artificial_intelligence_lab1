package ru.bstu.ai.core;

import ru.bstu.ai.core.enums.Position;
import ru.bstu.ai.core.model.*;
import ru.bstu.ai.core.service.Solution;
import ru.bstu.ai.core.service.SolutionDepthImpl;

public class MainTest {

    public static void main(String[] args) {
        Field field = new Field("file.txt");
        State state = new State(new Cupboard(0, 1, Position.HORIZONTAL), field);

        Solution solution = new SolutionDepthImpl();
        Statistic solve = solution.solve(state);

        solve.printStat();

    }
}
