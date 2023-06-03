package com.example.dotogether.util.helper

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.alarms.NotificationAlarmReceiver
import com.example.dotogether.model.NotificationData
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.SharedPreferencesUtil
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.*
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.ParseException
import java.time.*
import java.time.format.TextStyle
import java.util.*
import kotlin.math.abs

object RuntimeHelper {
    var TAG = "bekbek"

    fun glideForPersonImage(context: Context) = glide(
        context,
        RequestOptions()
            .placeholder(R.drawable.account)
            .error(R.drawable.account)
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
        return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT)
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

    fun sendNotification(context: Context, activity: Class<*>, title: String?, messageBody: String?, notificationId: Int, notificationData: NotificationData? = null) {
        val intent = Intent(context, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        notificationData?.let {
            intent.putExtra(Constants.NOTIFICATION_DATA, it)
        }
        val pendingIntent = PendingIntent.getActivity(context, Random().nextInt(), intent, PendingIntent.FLAG_IMMUTABLE)

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

        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    fun shareAppLink(context: Context) {
        generateSharingLink(
            deepLink = Constants.PREFIX.toUri(),
        ) { generatedLink ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                generatedLink
            )

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }

    fun shareTargetLink(context: Context, targetId: Int) {
        generateSharingLink(
            deepLink = "${Constants.PREFIX}/target/${targetId}".toUri(),
        ) { generatedLink ->
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                generatedLink
            )

            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)))
        }
    }

    private fun generateSharingLink(
        deepLink: Uri,
        previewImageLink: Uri? = null,
        getShareableLink: (String) -> Unit = {},
    ) {
        FirebaseDynamicLinks.getInstance().createDynamicLink().run {
            // What is this link parameter? You will get to know when we will actually use this function.
            link = deepLink

            // [domainUriPrefix] will be the domain name you added when setting up Dynamic Links at Firebase Console.
            // You can find it in the Dynamic Links dashboard.
            domainUriPrefix = Constants.PREFIX

            // Pass your preview Image Link here;
//            setSocialMetaTagParameters(
//                DynamicLink.SocialMetaTagParameters.Builder()
//                    .setImageUrl(previewImageLink)
//                    .build()
//            )

            // Required
            androidParameters {
                build()
            }

            // Finally
            buildShortDynamicLink()
        }.also {
            it.addOnSuccessListener { dynamicLink ->
                // This lambda will be triggered when short link generation is successful
                // Retrieve the newly created dynamic link so that we can use it further for sharing via Intent.
                getShareableLink.invoke(dynamicLink.shortLink.toString())
            }
            it.addOnFailureListener {
                // This lambda will be triggered when short link generation failed due to an exception
                Log.d(TAG, "error generateSharingLink: ${it.message}")
                // Handle
            }
        }
    }

    fun extractTargetParam(intentData: String): Int? {
        val targetString = "target/"
        val targetIndex = intentData.indexOf(targetString, ignoreCase = true)
        if (targetIndex != -1) {
            val paramStartIndex = targetIndex + targetString.length
            val targetParam = intentData.substring(paramStartIndex)
            if (targetParam.all { it.isDigit() }) {
                return targetParam.toInt()
            }
        }
        return null
    }

    fun isSameDay(millis1: Long, millis2: Long): Boolean {
        val calendar1 = Calendar.getInstance()
        calendar1.timeInMillis = abs(millis1)

        val calendar2 = Calendar.getInstance()
        calendar2.timeInMillis = abs(millis2)

        return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
    }

    fun convertDateToLocalDate(date: Date): LocalDate {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
    }

    fun isDoItBTNOpen(target: Target): Boolean {
        return if (target.last_date != null) {
            val date = Constants.DATE_FORMAT_6.tryParse(target.last_date!!) ?: return true
            date.convertToLocalTimezone()
            when (target.period) {
                Constants.DAILY -> !isDateInCurrentType(date, Calendar.DAY_OF_YEAR)
                Constants.WEEKLY -> !isDateInCurrentType(date, Calendar.WEEK_OF_YEAR)
                Constants.MONTHLY -> !isDateInCurrentType(date, Calendar.MONTH)
                else -> !isDateInRangeForWeek(date, getRangeForPeriod(target.period))
            }
        } else {
            val todayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            val weekdays = getRangeForPeriod(target.period)
            if (weekdays.isNotEmpty() && target.period != Constants.MONTHLY) weekdays.contains(todayOfWeek) else true
        }
    }

    private fun getRangeForPeriod(period: String?): List<Int> {
        val weekdays = arrayOf(
            Constants.MON to 2, Constants.TUE to 3, Constants.WED to 4, Constants.THU to 5, Constants.FRI to 6, Constants.SAT to 7, Constants.SUN to 1
        )
        return when (period) {
            Constants.MONDAY_TO_FRIDAY -> listOf(2, 3, 4, 5, 6)
            null -> emptyList()
            else -> {
                weekdays.filter { (day, _) -> period.contains(day) }.map { (_, index) -> index }
            }
        }
    }

    private fun isDateInCurrentType(date: Date, calenderType: Int): Boolean {
        val currentDateOrder = Calendar.getInstance().get(calenderType)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val dateOrder = Calendar.getInstance().apply { time = date }.get(calenderType)
        val dateYear = Calendar.getInstance().apply { time = date }.get(Calendar.YEAR)
        return dateOrder == currentDateOrder && dateYear == currentYear
    }

    private fun isDateInRangeForWeek(date: Date, weekdays: List<Int>): Boolean {
        val dayOfWeek = Calendar.getInstance().apply { time = date }.get(Calendar.DAY_OF_WEEK)
        val todayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return !weekdays.contains(todayOfWeek) || (dayOfWeek == todayOfWeek && isDateInCurrentType(date, Calendar.WEEK_OF_YEAR))
    }

    fun animateBackgroundColorChange(view: View, colorRes: Int, duration: Long) {
        val originalColor = Color.TRANSPARENT
        val colorTo = ContextCompat.getColor(view.context, colorRes)

        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorTo, originalColor)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener { animator ->
            val value = animator.animatedValue as Int
            view.setBackgroundColor(value)
        }
        colorAnimation.start()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setPeriodAlarmManager(context: Context) {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val isAlarmSet = SharedPreferencesUtil.getBoolean(context, "isAlarmSet", false)
        if (!isAlarmSet) {
            Log.d(TAG, "setAlarmManager")
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                getNotificationCalender().timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            SharedPreferencesUtil.setBoolean(context, "isAlarmSet", true)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarmManager(context: Context) {
        val alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        Log.d(TAG, "setAlarmManager")
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            getNotificationCalender().timeInMillis,
            pendingIntent
        )
    }

    fun getNotificationCalender(): Calendar {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
//            set(Calendar.MINUTE, (0..59).random())
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (calendar.timeInMillis - System.currentTimeMillis() <= 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "getNotificationCalender time: ${Constants.DATE_FORMAT_6.format(Date(calendar.timeInMillis))}")
        return calendar
    }

    fun Date.convertToLocalTimezone() {
        // Cihazın bulunduğu varsayılan saat dilimini al
        val deviceZoneId = ZoneId.systemDefault()

        // Cihazın varsayılan saat dilimindeki saat farkını hesapla
        val deviceOffset = deviceZoneId.rules.getOffset(Instant.ofEpochMilli(this.time))

        val convertedDate = when {
            deviceOffset.totalSeconds > 0 -> Date(this.time + deviceOffset.totalSeconds * 1000)
            else -> Date(this.time - deviceOffset.totalSeconds * 1000)
        }

        this.time = convertedDate.time
    }

    fun Date.convertToGMT0Timezone() {
        // Cihazın bulunduğu varsayılan saat dilimini al
        val deviceZoneId = ZoneId.systemDefault()

        // Cihazın varsayılan saat dilimindeki saat farkını hesapla
        val deviceOffset = deviceZoneId.rules.getOffset(Instant.ofEpochMilli(this.time))

        val convertedDate = when {
            deviceOffset.totalSeconds > 0 -> Date(this.time - deviceOffset.totalSeconds * 1000)
            else -> Date(this.time + deviceOffset.totalSeconds * 1000)
        }

        this.time = convertedDate.time
    }

    fun getCurrentTimeGMT0(): Date {
        val date = Date()
        date.convertToGMT0Timezone()
        return date
    }
}