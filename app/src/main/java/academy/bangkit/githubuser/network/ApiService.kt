package academy.bangkit.githubuser.network

import academy.bangkit.githubuser.model.SearchResponse
import academy.bangkit.githubuser.model.User
import academy.bangkit.githubuser.model.UserDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    fun getSearchUsers(
        @Query("q") username: String,
        @Header("Authorization") token: String
    ): Call<SearchResponse>

    @GET("/users/{username}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetail>

    @GET("/users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<User>>

    @GET("/users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}