// Object representing a small in-memory test database

package comp2850.music.db

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object TestDatabase {
    const val URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;"
    const val DRIVER = "org.h2.Driver"

    val db by lazy {
        Database.connect(URL, DRIVER)
    }

    fun create() {
        transaction(db) {
            SchemaUtils.drop(ArtistTable, AlbumTable)
            SchemaUtils.create(ArtistTable, AlbumTable)

            val band = ArtistTable.insertAndGetId {
                it[name] = "A Band"
            }

            val soloArtist = ArtistTable.insertAndGetId {
                it[name] = "Doe, John"
                it[isSolo] = true
            }

            AlbumTable.insert {
                it[title] = "An Album"
                it[artist] = band
                it[year] = 2025
            }

            AlbumTable.insert {
                it[title] = "First Album"
                it[artist] = soloArtist
                it[year] = 2019
            }

            AlbumTable.insert {
                it[title] = "Second Album"
                it[artist] = soloArtist
                it[year] = 2023
            }
        }
    }
}
