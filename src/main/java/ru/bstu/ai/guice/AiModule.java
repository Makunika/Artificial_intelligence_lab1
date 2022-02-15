package ru.bstu.ai.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import ru.bstu.ai.core.service.Solution;
import ru.bstu.ai.core.service.SolutionDepthImpl;
import ru.bstu.ai.core.service.SolutionWideImpl;

/**
 * инъекция всяких зависимостей (наших сервисов для ui).
 */
public class AiModule extends AbstractModule {
    @Override
    protected void configure() {

        bind(Solution.class)
                .to(SolutionWideImpl.class);

        bind(Solution.class)
                .annotatedWith(Names.named("Depth"))
                .to(SolutionDepthImpl.class)
                .in(Scopes.SINGLETON);

        bind(Solution.class)
                .annotatedWith(Names.named("Wide"))
                .to(SolutionWideImpl.class)
                .in(Scopes.SINGLETON);
    }
}
