package ru.bstu.ai.ui

import com.google.inject.Guice
import com.google.inject.Key
import com.google.inject.name.Names
import javafx.stage.Stage
import ru.bstu.ai.core.service.Solution
import ru.bstu.ai.guice.AiModule
import ru.bstu.ai.ui.views.MainView
import tornadofx.App
import tornadofx.DIContainer
import tornadofx.FX
import kotlin.reflect.KClass

class AiApplication: App(MainView::class) {

    init {
        val guice = Guice.createInjector(AiModule())
        FX.dicontainer = object : DIContainer {
            override fun <T : Any> getInstance(type: KClass<T>)
                    = guice.getInstance(type.java)

            override fun <T : Any> getInstance(type: KClass<T>, name: String)
                    = guice.getInstance(Key.get(type.java, Names.named(name)))
        }
    }

    override fun start(stage: Stage) {
        stage.centerOnScreen()
        super.start(stage)
    }
}