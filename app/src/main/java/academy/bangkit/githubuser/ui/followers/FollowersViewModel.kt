package academy.bangkit.githubuser.ui.followers

import academy.bangkit.githubuser.BuildConfig
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.data.remote.retrofit.ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val tokenApi = BuildConfig.API_KEY
    private val listFollowers = MutableLiveData<ArrayList<UserResponse>>()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isNoFollowers = MutableLiveData<Boolean>()
    val isNoFollowers: LiveData<Boolean> get() = _isNoFollowers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun setUserFollowers(username: String) {
        val client = ApiConfig.getApiService().getUserFollowers(username, tokenApi)
        client.enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(call: Call<List<UserResponse>>, response: Response<List<UserResponse>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _isError.postValue(false)
                    response.body()?.apply {
                        _isNoFollowers.postValue(isEmpty())
                        listFollowers.postValue(this as ArrayList<UserResponse>)
                    }
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        })
    }

    fun getUserFollowers(): LiveData<ArrayList<UserResponse>> {
        return listFollowers
    }
}