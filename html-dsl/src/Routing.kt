import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.util.getValue
import kotlinx.html.*

fun Application.configureRouting() {
    routing {
        get("/roll/d{sides}") {
            val sides: Int by call.parameters
            val number = dieRoll(sides)
            call.respondHtml(HttpStatusCode.OK) {
                head {
                    meta(charset = "utf-8")
                    meta(name = "viewport", content = "width=device-width, initial-scale=1")
                    meta(name = "color-scheme", content = "light dark")
                    link(rel = "stylesheet", href = "https://cdn.jsdelivr.net/npm/@picocss/pico@2/css/pico.min.css")
                    title { +"Die Roller" }
                }
                body {
                    main(classes = "container") {
                        h1 { +"d$sides Dice Roll" }
                        p { +"Result = $number" }
                    }
                }
            }
        }
    }
}
