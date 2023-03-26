package com.example.dotogether.util

import android.content.Context
import com.example.dotogether.R
import java.util.regex.Pattern

object ValidationFactory {

    private val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"
    )

    private val PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,20}$"
    )

    fun validMail(email: String?) : Resource<String> {
        if (email != null && EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return Resource.Success()
        }
        return Resource.Error()
    }

    fun validPassword(password: String, context: Context) : Resource<String> {
        if (PASSWORD_PATTERN.matcher(password).matches()) {
            return Resource.Success()
        }
        if (password.length !in 8..20) {
            return Resource.Error(message = context.getString(R.string.password_size_error))
        }
        if (!password.any { it.isDigit() }) {
            return Resource.Error(message = context.getString(R.string.password_digit_error))
        }
        if (!password.any { it.isUpperCase() }) {
            return Resource.Error(message = context.getString(R.string.password_upper_case_error))
        }
        if (!password.any { it.isLowerCase() }) {
            return Resource.Error(message = context.getString(R.string.password_lower_case_error))
        }
        return Resource.Error()
    }

    fun validVerifyCode(verifyCode: String?, context: Context) : Resource<String> {
        if (verifyCode != null && verifyCode.isNotEmpty() && verifyCode.any { it.isDigit() } && verifyCode.length == 4) {
            return Resource.Success()
        }
        return Resource.Error(message = context.getString(R.string.wrong_verify_code))
    }
}