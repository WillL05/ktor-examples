// Object representing a small test database

package comp2850.music.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object TestDatabase {
    const val URL = "jdbc:sqlite:file:test.db"
    const val DRIVER = "org.sqlite.JDBC"

    val db by lazy {
        Database.connect(URL, DRIVER)
    }

    fun create() {
        transaction(db) {
            SchemaUtils.drop(Artists, Albums)
            SchemaUtils.create(Artists, Albums)

            val artist1 = Artists.insert {
                it[name] = "A Band"
            } get Artists.id

            val artist2 = Artists.insert {
                it[name] = "Doe, John"
                it[isSolo] = true
            } get Artists.id

            Albums.insert {
                it[title] = "An Album"
                it[artist] = artist1
                it[year] = 2025
            }

            Albums.insert {
                it[title] = "First Album"
                it[artist] = artist2
                it[year] = 2019
            }

            Albums.insert {
                it[title] = "Second Album"
                it[artist] = artist2
                it[year] = 2023
            }
        }
    }
}
