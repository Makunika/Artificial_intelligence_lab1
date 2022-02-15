package ru.bstu.ai.ui.views

import tornadofx.*

class MainView : View("Искусственный интеллект") {
    override val root = borderpane {
        center {
            text("Приветики")
        }
    }
}