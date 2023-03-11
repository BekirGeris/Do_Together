package com.example.dotogether.util.helper

import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Base64
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.view.activity.HomeActivity
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.ParseException
import java.time.Month
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.*

object RuntimeHelper {
    var TAG = "bekbek"

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

    fun glide(context: Context, requestOptions: RequestOptions): RequestManager {
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

    fun Dialog.tryShow() {
        try {
            this.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Date.isToday(): Boolean {
        val today = Calendar.getInstance().time
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Date.isYesterday(): Boolean {
        val yesterday = Calendar.getInstance().apply { add(Calendar.DATE, -1) }.time
        return this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() == yesterday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun View.setViewProperties(isEnabled: Boolean) {
        this.isEnabled = isEnabled
        this.isClickable = isEnabled
        this.alpha = if (isEnabled) 1f else 0.6f
    }

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    fun sendNotification(context: Context, title: String, messageBody: String) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(Random().nextInt(), notificationBuilder.build())
    }

    fun shareTargetLink(context: Context, targetId: Int) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"https://toggether.bg/target/$targetId")

        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
    }

}