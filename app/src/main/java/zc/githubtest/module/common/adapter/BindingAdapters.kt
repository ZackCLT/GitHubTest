package zc.githubtest.module.common.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import zc.githubtest.R


@BindingAdapter("avatarUrl")
fun bindImageFromUrl(view: ImageView, avatarUrl: String?) {
    if (!avatarUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(avatarUrl)
            .placeholder(R.drawable.ic_circle_gray_r180_bg)
            .apply(RequestOptions().circleCrop())
            .into(view)
    }
}