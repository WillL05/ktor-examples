// Artist entity, mapping onto Artists table

package comp2850.music.db

import org.jetbrains.exposed.dao.UIntEntity
import org.jetbrains.exposed.dao.UIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Artist(id: EntityID<UInt>): UIntEntity(id) {
    companion object: UIntEntityClass<Artist>(Artists)

    var name by Artists.name
    var isSolo by Artists.isSolo
    var info by Artists.info
    val albums by Album referrersOn Albums.artist

    val properName: String get() {
        // Converts "family-name, given-name" format of solo artist names
        // into the more familiar "given-name family-name" format
        if (isSolo) {
            val parts = name.split(",").map { it.trim() }
            return "${parts[1]} ${parts[0]}"
        }
        return name
    }

    override fun toString() = name
}
