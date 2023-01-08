package com.example.dotogether.util

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

    fun validMail(email: String) : Resource<String> {
        if (EMAIL_ADDRESS_PATTERN.matcher(email).matches()) {
            return Resource.Success()
        }
        return Resource.Error()
    }

    fun validPassword(password: String) : Resource<String> {
        if (PASSWORD_PATTERN.matcher(password).matches()) {
            return Resource.Success()
        }
        if (password.length !in 8..20) {
            return Resource.Error(message = "Password must be at least 8 characters")
        }
        if (!password.any { it.isDigit() }) {
            return Resource.Error(message = "Password must contain numbers")
        }
        if (!password.any { it.isUpperCase() }) {
            return Resource.Error(message = "Password must contain at least one uppercase letter")
        }
        if (!password.any { it.isLowerCase() }) {
            return Resource.Error(message = "Password must contain at least one lowercase letter")
        }
        return Resource.Error()
    }

    fun validTarget() : Resource<String> {

        return Resource.Error()
    }
}