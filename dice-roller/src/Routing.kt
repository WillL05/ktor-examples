// Set up application routing and request handling

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtmlTemplate
import io.ktor.server.request.ApplicationRequest
import io.ktor.server.request.uri
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        get("/") { call.displayForm() }
        get("/roll") { call.handleDiceRoll() }
    }
}

private suspend fun ApplicationCall.displayForm() {
    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Dice Roller" }
        content {
            h1 { +"Dice Roller" }
            form(action = "/roll", method = FormMethod.get) {
                label {
                    htmlFor = "dice"
                    +"Number of dice"
                }
                numberInput {
                    id = "dice"
                    name = "dice"
                    min = "1"
                    max = "10"
                    value = "3"
                    required = true
                }
                label {
                    htmlFor = "sides"
                    +"Number of sides"
                }
                select {
                    id = "sides"
                    name = "sides"
                    required = true
                    sidesOptions.forEach { value ->
                        option {
                            if (value == 6) { selected = true }
                            +"$value"
                        }
                    }
                }
                button { +"Roll Dice" }
            }
        }
    }
}

private suspend fun ApplicationCall.handleDiceRoll() {
    val (dice, sides) = queryParameters(request)
    val results = diceRoll(dice, sides)

    respondHtmlTemplate(LayoutTemplate()) {
        titleText { +"Dice Results" }
        content {
            h1 { +"Dice Results" }

            p { +"You rolled ${dice}d$sides" }

            p {
                +"The result was: "
                strong {
                    +"${results[0]}"
                    for (result in results.drop(1)) {
                        +", $result"
                    }
                }
            }

            p {
                +"For a total of "
                strong{ +"${results.sum()}" }
            }

            p {
                +"You can "
                a(request.uri) { +"repeat this roll" }
                +", or request a "
                a("/") { +"new roll" }
                +"."
            }
        }
    }
}

private fun queryParameters(request: ApplicationRequest) = Pair(
    request.queryParameters["dice"]?.toInt() ?: error("Number of dice not specified"),
    request.queryParameters["sides"]?.toInt() ?: error("Number of sides not specified")
)
