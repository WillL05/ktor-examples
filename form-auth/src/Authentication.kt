// Configuration for forms-based and session-based authentication

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.form
import io.ktor.server.auth.session
import io.ktor.server.pebble.respondTemplate
import io.ktor.server.response.respondRedirect

fun Application.configureAuthentication() {
    install(Authentication) {
        form("auth-form") {
            userParamName = "username"
            passwordParamName = "password"
            validate { credentials ->
                if (UserDatabase.checkCredentials(credentials)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
            challenge {
                call.respondTemplate("login.peb", mapOf(
                    "error" to "Invalid username or password!"
                ))
            }
        }
        session<UserSession>("auth-session") {
            validate { session ->
                if(sessionOK(session)) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}

fun sessionOK(session: UserSession): Boolean {
    // TODO: handle session checking properly
    return session.username == "nick"
}
