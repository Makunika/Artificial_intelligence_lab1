package ru.bstu.ai.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import ru.bstu.ai.core.service.*;

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
                .annotatedWith(Names.named("A"))
                .to(SolutionABeforeSearhImpl.class)
                .in(Scopes.SINGLETON);

        bind(Solution.class)
                .annotatedWith(Names.named("SMA"))
                .to(SolutionSMASearchImpl.class)
                .in(Scopes.SINGLETON);

        bind(Solution.class)
                .annotatedWith(Names.named("Wide"))
                .to(SolutionWideImpl.class)
                .in(Scopes.SINGLETON);
    }
}
