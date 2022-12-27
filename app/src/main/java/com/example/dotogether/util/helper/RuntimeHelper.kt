package com.example.dotogether.util.helper

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R

object RuntimeHelper {
    var TOKEN = ""

    fun glide(context: Context)  = Glide
        .with(context)
        .setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )
}