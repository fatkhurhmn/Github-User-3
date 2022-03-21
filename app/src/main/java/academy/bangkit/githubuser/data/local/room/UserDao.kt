package academy.bangkit.githubuser.data.local.room

import academy.bangkit.githubuser.data.local.entity.UserEntity
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("SELECT * FROM user")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Query("SELECT EXISTS(SELECT * FROM user WHERE id = :id)")
    fun isUserFavorite(id: Int): LiveData<Boolean>
}