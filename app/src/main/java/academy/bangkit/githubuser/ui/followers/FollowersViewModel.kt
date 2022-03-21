package academy.bangkit.githubuser.ui.followers

import academy.bangkit.githubuser.BuildConfig
import academy.bangkit.githubuser.model.User
import academy.bangkit.githubuser.network.ApiConfig
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel : ViewModel() {

    private val tokenApi = BuildConfig.API_KEY
    private val listFollowers = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isNoFollowers = MutableLiveData<Boolean>()
    val isNoFollowers: LiveData<Boolean> get() = _isNoFollowers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    fun setUserFollowers(username: String) {
        val client = ApiConfig.getApiService().getUserFollowers(username, tokenApi)
        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _isError.postValue(false)
                    response.body()?.apply {
                        _isNoFollowers.postValue(isEmpty())
                        listFollowers.postValue(this as ArrayList<User>)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
            }
        })
    }

    fun getUserFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }
}