package academy.bangkit.githubuser.ui.detail

import academy.bangkit.githubuser.BuildConfig
import academy.bangkit.githubuser.data.remote.response.UserDetailResponse
import academy.bangkit.githubuser.data.remote.retrofit.ApiConfig
import academy.bangkit.githubuser.utils.Event
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailViewModel : ViewModel() {

    private val tokenApi = BuildConfig.API_KEY
    private val userDetail = MutableLiveData<UserDetailResponse>()

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> get() = _isError

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> get() = _message

    fun setUserDetail(username: String) {
        val client = ApiConfig.getApiService().getUserDetail(username, tokenApi)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(call: Call<UserDetailResponse>, response: Response<UserDetailResponse>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _isError.postValue(false)
                    userDetail.postValue(response.body())
                } else {
                    _isError.postValue(true)
                    _message.postValue(Event(response.message()))
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _isError.postValue(true)
                _message.postValue(Event(t.message!!))
            }
        })
    }

    fun getUserDetail(): LiveData<UserDetailResponse> {
        return userDetail
    }
}