// Request handlers for album details
// (See also albums.peb and album.peb in resources/templates)

package comp2850.music.server

import comp2850.music.db.Album
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.pebble.respondTemplate
import io.ktor.server.response.respond
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun ApplicationCall.albums() {
    newSuspendedTransaction {
        val comparator = compareBy<Album> { it.artist.name }.thenBy { it.year }
        val albums = Album.all().sortedWith(comparator).toList()
        respondTemplate("albums.peb", mapOf("albums" to albums))
    }
}

suspend fun ApplicationCall.album() {
    newSuspendedTransaction {
        val result = runCatching {
            parameters["id"]?.let {
                Album.findById(it.toUInt())
            }
        }

        when (val album = result.getOrNull()) {
            null -> respond(HttpStatusCode.NotFound)
            else -> respondTemplate("album.peb", mapOf(
                "artist" to album.artist,
                "title" to album.title,
                "year" to album.year,
                "youtube" to (album.youtube ?: "")
            ))
        }
    }
}
