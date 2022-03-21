package academy.bangkit.githubuser.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse(

    @field:SerializedName("name")
    val name: String = "",

    @field:SerializedName("avatar_url")
    val avatarUrl: String = "",

    @field:SerializedName("public_repos")
    val publicRepos: Int = 0,

    @field:SerializedName("followers")
    val followers: Int = 0,

    @field:SerializedName("following")
    val following: Int = 0,

    @field:SerializedName("blog")
    val blog: String? = "",

    @field:SerializedName("company")
    val company: String? = "",

    @field:SerializedName("location")
    val location: String? = "",
) : Parcelable
