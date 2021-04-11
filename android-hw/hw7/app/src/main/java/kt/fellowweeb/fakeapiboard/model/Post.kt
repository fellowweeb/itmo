package kt.fellowweeb.fakeapiboard.model

import android.os.Parcelable
import io.realm.RealmObject
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Post(
    var id: Int? = null,
    var title: String = "",
    var body: String = "",
    var userId: Int? = null
) : RealmObject(), Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        if (id != other.id) return false
        if (title != other.title) return false
        if (body != other.body) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + (userId ?: 0)
        return result
    }
}