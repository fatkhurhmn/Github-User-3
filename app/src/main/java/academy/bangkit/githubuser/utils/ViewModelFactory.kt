package academy.bangkit.githubuser.utils

import academy.bangkit.githubuser.ui.detail.UserDetailViewModel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory private constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserDetailViewModel::class.java)) {
            return UserDetailViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(application: Application): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(application)
        }.also { instance = it }
    }
}