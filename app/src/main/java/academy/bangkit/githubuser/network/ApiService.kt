package academy.bangkit.githubuser.network

import academy.bangkit.githubuser.BuildConfig
import academy.bangkit.githubuser.model.SearchResponse
import academy.bangkit.githubuser.model.User
import academy.bangkit.githubuser.model.UserDetail
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/search/users")
    fun getSearchUsers(
        @Query("q") username: String,
        @Header("Authorization") token: String
    ): Call<SearchResponse>

    @GET("/users/{username}")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getUserDetail(
        @Path("username") username: String,
    ): Call<UserDetail>

    @GET("/users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getUserFollowers(
        @Path("username") username: String,
    ): Call<List<User>>

    @GET("/users/{username}/following")
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    fun getUserFollowing(
        @Path("username") username: String,
    ): Call<List<User>>
}