package academy.bangkit.githubuser.data

import academy.bangkit.githubuser.data.local.entity.UserEntity
import academy.bangkit.githubuser.data.local.room.UserDao
import academy.bangkit.githubuser.data.local.room.UserDatabase
import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository (application: Application) {
    private val userDao: UserDao
    private val executors: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserDatabase.getInstance(application)
        userDao = db.userDao()
    }

    fun getFavoriteUser(): LiveData<List<UserEntity>> = userDao.getFavoriteUser()

    fun insert(user: UserEntity) {
        executors.execute {
            userDao.insert(user)
        }
    }

    fun delete(user: UserEntity) {
        executors.execute {
            userDao.delete(user)
        }
    }

    fun isFavoriteUser(id: Int): LiveData<Boolean> = userDao.isUserFavorite(id)
}