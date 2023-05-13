package com.example.smartlamp.utils

import android.content.Context
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

class Validations {

    companion object{

        fun checkEmailValidates(username: String, emailTextInputLayout: TextInputLayout, context: Context): Boolean {
            var isError = false
            val emailPattern = Patterns.EMAIL_ADDRESS
            if (username.isEmpty()) {
                isError = true
            } else if (!username.matches(emailPattern.toRegex())) {
                isError = true
                emailTextInputLayout.error = "Please enter a valid email address"
            }
            return isError
        }

        fun passwordValidate(
            pass: String,
            passwordTextInputLayout: TextInputLayout
        ): Boolean {
            var isError = false
            if (pass.isEmpty()) {
                isError = true
                passwordTextInputLayout.error = "Enter your password"
            } else if (pass.length < 8) {
                isError = true
                passwordTextInputLayout.error = "Password must have at least 8 characters"
            }
            return isError
        }

        fun nameValidate(
            name: String,
            nameTextInputLayout: TextInputLayout
        ): Boolean {
            var isError = false
            val pattern = Regex("^[A-Z][a-zA-Z]*$")

            if (name.isEmpty()) {
                isError = true
                nameTextInputLayout.error = "Enter your first name"
            } else if (!pattern.matches(name)) {
                isError = true
                nameTextInputLayout.error = "Your name is Invalid"
            }
            return isError
        }
    }

}