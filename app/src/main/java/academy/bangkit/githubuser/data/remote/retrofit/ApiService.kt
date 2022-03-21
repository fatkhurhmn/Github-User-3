package academy.bangkit.githubuser.data.remote.retrofit

import academy.bangkit.githubuser.data.remote.response.SearchResponse
import academy.bangkit.githubuser.data.remote.response.UserResponse
import academy.bangkit.githubuser.data.remote.response.UserDetailResponse
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
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<UserDetailResponse>

    @GET("/users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<UserResponse>>

    @GET("/users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String,
        @Header("Authorization") token: String
    ): Call<List<UserResponse>>
}