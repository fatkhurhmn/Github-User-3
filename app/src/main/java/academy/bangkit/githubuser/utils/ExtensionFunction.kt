package academy.bangkit.githubuser.utils

import academy.bangkit.githubuser.R
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object ExtensionFunction {
    fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500).placeholder(R.drawable.ic_default_photo))
            .centerCrop()
            .into(this)
    }
}