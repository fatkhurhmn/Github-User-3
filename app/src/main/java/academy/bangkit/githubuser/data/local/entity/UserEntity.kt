package academy.bangkit.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int? = 0,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "photo")
    var photo: String? = null
)