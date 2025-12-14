// Creates the Music Club database

package comp2850.music.db

fun main() {
    println("Creating ${MusicDatabase.URL}...")
    MusicDatabase.create()
}
