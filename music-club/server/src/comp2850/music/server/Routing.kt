// Routing for Music Club app
// (Request handling in Home.kt, Artists.kt, Albums.kt)

package comp2850.music.server

import io.ktor.server.application.Application
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import org.jetbrains.exposed.sql.Database

fun Application.configureRouting(db: Database) {
    routing {
        get("/") { call.homePage() }
        post("/search") { call.searchResults() }
        get("/artists") { call.artists() }
        get("/artists/{id}") { call.artist() }
        get("/albums") { call.albums() }
        get("/albums/{id}") { call.album() }
    }
}
