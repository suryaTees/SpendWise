package com.example.spendwise.utils

import android.util.Patterns

fun validateEmail(email: String): String? {
    return when {
        email.isBlank() -> "Email is required"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Enter a valid email"
        else -> null
    }
}

fun validatePassword(password: String): String? {
    return when {
        password.isBlank() -> "Password is required"
        password.length < 6 -> "Password must be at least 6 characters"
        else -> null
    }
}
