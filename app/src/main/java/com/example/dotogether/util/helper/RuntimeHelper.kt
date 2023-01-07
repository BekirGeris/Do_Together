package com.example.dotogether.util.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.ParseException
import java.util.*

object RuntimeHelper {
    var TOKEN = ""

    fun glideForPersonImage(context: Context) = glide(
        context,
        RequestOptions()
            .placeholder(R.drawable.ic_account)
            .error(R.drawable.ic_account)
    )

    fun glideForImage(context: Context) = glide(
        context,
        RequestOptions()
            .placeholder(
                CircularProgressDrawable(context).apply {
                    strokeWidth = 8f
                    centerRadius = 40f
                    start()
                })
            .error(R.drawable.error_image_background)
    )

    private fun glide(context: Context, requestOptions: RequestOptions): RequestManager {
        return Glide
            .with(context)
            .setDefaultRequestOptions(requestOptions)
    }

    fun imageToBase64(filePath: String) : String {
        val bitmap = BitmapFactory.decodeFile(filePath)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
        val bytes: ByteArray = stream.toByteArray()
        return  "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun DateFormat.tryParse(source: String) : Date? {
        try {
            return this.parse(source)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}