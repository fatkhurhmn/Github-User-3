package academy.bangkit.githubuser.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class SearchResponse(

    @field:SerializedName("items")
    val users: List<UserResponse>,
) : Parcelable
