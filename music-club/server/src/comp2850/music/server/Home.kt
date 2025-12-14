// Request handlers for Music Club home page
// (See also index.peb and search.peb in resources/templates)

package comp2850.music.server

import comp2850.music.db.Album
import comp2850.music.db.Albums
import comp2850.music.db.Artist
import io.ktor.server.application.ApplicationCall
import io.ktor.server.pebble.respondTemplate
import io.ktor.server.request.receiveParameters
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun ApplicationCall.homePage() {
    newSuspendedTransaction {
        val data = mapOf("albums" to Album.count(), "artists" to Artist.count())
        respondTemplate("index.peb", data)
    }
}

suspend fun ApplicationCall.searchResults() {
    newSuspendedTransaction {
        val formParams = receiveParameters()
        val searchTerm = formParams["search_term"] ?: ""
        val results = Album.find { Albums.title like "%$searchTerm%" }
        val data = mapOf("searchTerm" to searchTerm, "results" to results)
        respondTemplate("search.peb", data)
    }
}
