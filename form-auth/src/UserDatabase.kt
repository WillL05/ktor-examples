// Data store for usernames and passwords

import io.ktor.server.auth.UserPasswordCredential
import java.security.MessageDigest

const val MIN_USERNAME_LENGTH = 4
const val MIN_PASSWORD_LENGTH = 8

fun UserPasswordCredential.nameIsValid() = when {
    name.length < MIN_USERNAME_LENGTH -> false
    name.all { it.isLetterOrDigit() || it == '_' } -> true
    else -> false
}

fun UserPasswordCredential.passwordIsValid() = when {
    password.length < MIN_PASSWORD_LENGTH -> false
    password.any { it.isWhitespace() } -> false
    else -> true
}

object UserDatabase {
    // TODO: make this persistent (CSV file?)
    // TODO: use proper password hashing algorithm (many iterations & salt)

    private val passwordMap = mutableMapOf<String, String>()

    val size get() = passwordMap.size

    fun addUser(cred: UserPasswordCredential) {
        require(cred.nameIsValid()) { "Invalid username" }
        require(cred.name !in passwordMap) { "Username already exists" }
        require(cred.passwordIsValid()) { "Invalid password" }
        passwordMap[cred.name] = hashOf(cred.password)
    }

    fun checkCredentials(cred: UserPasswordCredential): Boolean {
        return cred.name in passwordMap && hashOf(cred.password) == passwordMap[cred.name]
    }

    private fun hashOf(password: String): String {
        val sha256 = MessageDigest.getInstance("SHA-256")
        return sha256.digest(password.toByteArray()).toHexString()
    }
}
