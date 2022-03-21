package academy.bangkit.githubuser.ui.following

import academy.bangkit.githubuser.BuildConfig
import academy.bangkit.githubuser.model.User
import academy.bangkit.githubuser.network.ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {

    private val tokenApi = BuildConfig.API_KEY
    private val listFollowing = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isNoFollowing = MutableLiveData<Boolean>()
    val isNoFollowing: LiveData<Boolean> get() = _isNoFollowing

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun setUsersFollowing(username: String) {
        val client = ApiConfig.getApiService().getUserFollowing(username, tokenApi)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _isError.postValue(false)
                    response.body()?.apply {
                        _isNoFollowing.postValue(isEmpty())
                        listFollowing.postValue(this as ArrayList<User>)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        })
    }

    fun getUserFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}