package academy.bangkit.githubuser.ui.favorite

import academy.bangkit.githubuser.data.UserRepository
import academy.bangkit.githubuser.data.local.entity.UserEntity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class FavoriteViewModel(application: Application): ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)

    fun getFavoriteUser(): LiveData<List<UserEntity>> = userRepository.getFavoriteUser()
}