// Set up application routing & request handling

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.log
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.UserPasswordCredential
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.pebble.respondTemplate
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.util.getOrFail

fun Application.configureRouting() {
    routing {
        get("/") { call.homePage() }
        get("/login") { call.loginPage() }
        get("/register") { call.registrationPage() }
        post("/register") { call.registerUser() }
        get("/logout") { call.logout() }
        authenticate("auth-form") {
            post("/login") { call.login() }
        }
        authenticate("auth-session") {
            get("/private") { call.privatePage() }
        }
    }
}

private suspend fun ApplicationCall.homePage() {
    respondTemplate("index.peb", model = mapOf(
        "numUsers" to UserDatabase.size
    ))
}

private suspend fun ApplicationCall.registrationPage() {
    respondTemplate("register.peb", model = mapOf())
}

private suspend fun ApplicationCall.registerUser() {
    val credentials = getCredentials()
    val result = runCatching {
        UserDatabase.addUser(credentials)
    }
    if (result.isSuccess) {
        application.log.info("User ${credentials.name} registered (DB size = ${UserDatabase.size})")
        respondRedirect("/")
    }
    else {
        val error = result.exceptionOrNull()?.message ?: ""
        respondTemplate("register.peb", model = mapOf("error" to error))
    }
}

private suspend fun ApplicationCall.getCredentials(): UserPasswordCredential {
    val formParams = receiveParameters()
    val username = formParams.getOrFail("username")
    val password = formParams.getOrFail("password")
    return UserPasswordCredential(username, password)
}

private suspend fun ApplicationCall.loginPage() {
    respondTemplate("login.peb", model = mapOf())
}

private suspend fun ApplicationCall.login() {
    val username = principal<UserIdPrincipal>()?.name.toString()
    sessions.set(UserSession(username, 1))
    respondRedirect("/private")
}

private suspend fun ApplicationCall.logout() {
    sessions.clear<UserSession>()   // clear named session rather than all?
    respondRedirect("/")
}

private suspend fun ApplicationCall.privatePage() {
    val session = principal<UserSession>()
    // Increment visit count and update session cookie
    sessions.set(session?.copy(count = session.count + 1))

    respondTemplate("private.peb", model = mapOf(
        "username" to session?.username.toString(),
        "visits" to (session?.count ?: 0),
    ))
}
