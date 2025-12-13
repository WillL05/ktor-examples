// Request handlers for artist details
// (See also artists.peb and artist.peb in resources/templates)

package comp2850.music.server

import comp2850.music.db.Artist
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.pebble.respondTemplate
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun ApplicationCall.artists() {
    newSuspendedTransaction {
        val artists = Artist.all().sortedBy { it.name }.toList()
        respondTemplate("artists.peb", mapOf("artists" to artists))
    }
}

suspend fun ApplicationCall.artist() {
    newSuspendedTransaction {
        val result = runCatching {
            parameters["id"]?.let {
                Artist.findById(it.toUInt())
            }
        }

        when (val artist = result.getOrNull()) {
            null -> respond(HttpStatusCode.NotFound)
            else -> {
                val data = mapOf(
                    "name" to artist.name,
                    "solo" to artist.isSolo,
                    "info" to (artist.info ?: ""),
                    "albums" to artist.albums.sortedBy { it.year }.toList(),
                )
                respondTemplate("artist.peb", data)
            }
        }
    }
}
