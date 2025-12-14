// Database schema for Artists table

package comp2850.music.db

import org.jetbrains.exposed.dao.id.UIntIdTable

const val MAX_VARCHAR_LENGTH = 256

object Artists: UIntIdTable() {
    val name = varchar("name", MAX_VARCHAR_LENGTH)
    val isSolo = bool("is_solo").default(false)
    val info = varchar("info_url", MAX_VARCHAR_LENGTH).nullable()
}
