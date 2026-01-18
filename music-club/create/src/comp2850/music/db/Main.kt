// Creates the Music Club database

package comp2850.music.db

import org.apache.commons.csv.CSVFormat
import org.jetbrains.exposed.v1.core.StdOutSqlLogger
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.FileReader

const val ARTISTS_DATA = "csv/artists.csv"
const val ALBUMS_DATA = "csv/albums.csv"

typealias NameToIdMap = LinkedHashMap<String, EntityID<Int>>

fun main(args : Array<String>) {
    val logging = args.isNotEmpty() && args[0].lowercase() == "--sql"

    transaction(MusicDatabase.db) {
        if (logging) {
            addLogger(StdOutSqlLogger)
        }

        SchemaUtils.drop(ArtistTable, AlbumTable)
        SchemaUtils.create(ArtistTable, AlbumTable)

        val artists = addArtists(ARTISTS_DATA)
        addAlbums(ALBUMS_DATA, artists)
    }
}

fun addArtists(filename: String): NameToIdMap {
    FileReader(filename).use { reader ->
        val records = CSVFormat.DEFAULT.parse(reader).drop(1)
        val artists = NameToIdMap()
        for (record in records) {
            artists[record[0]] = ArtistTable.insertAndGetId {
                it[name] = record[0]
                it[isSolo] = record[1] == "S"
                it[info] = record[2].ifEmpty { null }
            }
        }
        return artists
    }
}

fun addAlbums(filename: String, artists: NameToIdMap) {
    FileReader(filename).use { reader ->
        val records = CSVFormat.DEFAULT.parse(reader).drop(1)
        for (record in records) {
            val artistId = artists.getOrElse(record[1]) {
                // Skip album if the named artist isn't already in the DB
                continue
            }
            AlbumTable.insert {
                it[title] = record[0]
                it[artist] = artistId
                it[year] = record[2].toInt()
                it[youtube] = youtubeUrl(record[3])
            }
        }
    }
}

fun youtubeUrl(id: String) = when {
    id.isNotBlank() -> "https://www.youtube.com/embed/videoseries?list=$id"
    else -> null
}
