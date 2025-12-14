// Album entity, mapping onto Albums table

package comp2850.music.db

import org.jetbrains.exposed.dao.UIntEntity
import org.jetbrains.exposed.dao.UIntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Album(id: EntityID<UInt>): UIntEntity(id) {
    companion object: UIntEntityClass<Album>(Albums)

    var artist by Artist referencedOn Albums.artist
    var title by Albums.title
    var year by Albums.year
    var youtube by Albums.youtube

    override fun toString() = title
}
