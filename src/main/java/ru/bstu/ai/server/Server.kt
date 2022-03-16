package ru.bstu.ai.server

import com.google.inject.Guice
import com.google.inject.Key
import com.google.inject.name.Names
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.bstu.ai.core.model.State
import ru.bstu.ai.core.service.Solution
import ru.bstu.ai.guice.AiModule
import java.io.File


fun server() {
    val injector = Guice.createInjector(AiModule())
    val solutionDepth = injector.getInstance(Key.get(Solution::class.java, Names.named("Depth")))
    val solutionWide = injector.getInstance(Key.get(Solution::class.java, Names.named("Wide")))
    val solutionA = injector.getInstance(Key.get(Solution::class.java, Names.named("A")))
    val solutionSMA = injector.getInstance(Key.get(Solution::class.java, Names.named("SMA")))

    val html = File("index.html").readText()
    val css = File("styles.css").readText()
    val js = File("main.js").readText()

    embeddedServer(Netty, port = 8080) {
        routing {
            get("/depth") {
                val solve = solutionDepth.solve(State("file.txt"))
                solve.printStat()

                call.respondText(contentType = ContentType.Application.Json, text = solve.toJson())
            }
            get("/wide") {
                val solve = solutionWide.solve(State("file.txt"))
                solve.printStat()

                call.respondText(contentType = ContentType.Application.Json, text = solve.toJson())
            }
            get("/a") {
                val solve = solutionA.solve(State("file.txt"))
                solve.printStat()

                call.respondText(contentType = ContentType.Application.Json, text = solve.toJson())
            }
            get("/sma") {
                val solve = solutionSMA.solve(State("file.txt"))
                solve.printStat()

                call.respondText(contentType = ContentType.Application.Json, text = solve.toJson())
            }
            get("/") {
                call.respondText(html, ContentType.Text.Html)
            }
            get("/main.js") {
                call.respondText(js, ContentType.Text.JavaScript)
            }
            get("/styles.css") {
                call.respondText(css, ContentType.Text.CSS)
            }
        }
    }.start(wait = true)
}