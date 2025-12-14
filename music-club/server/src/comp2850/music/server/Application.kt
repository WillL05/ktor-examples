// Application configuration & entry point

package comp2850.music.server

import comp2850.music.db.MusicDatabase
import comp2850.music.db.TestDatabase
import io.ktor.server.application.Application

fun Application.module() {
    configureTemplates()
    configureRouting(MusicDatabase.db)
}

fun Application.testModule() {
    configureTemplates()
    configureRouting(TestDatabase.db)
}

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}
