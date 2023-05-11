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
            if (pass.length < 8) {
                passwordTextInputLayout.error = "Password must have at least 8 characters"
                return false
            }
//            if (!pass.matches(".*\\d.*".toRegex())) {
//                passwordTextInputLayout.error = "Password must include one number"
//                return false
//            }
//            if (!pass.matches(".*[a-z].*".toRegex())) {
//                passwordTextInputLayout.error = "Password must include one lowercase letter"
//                return false
//            }
//            if (!pass.matches(".*[A-Z].*".toRegex())) {
//                passwordTextInputLayout.error = "Password must include one uppercase letter"
//                return false
//            }
//            if (!pass.matches(".*[!@#$%^&*+=?\"'(),-./:;<>_`{|}~-].*".toRegex())) {
//                passwordTextInputLayout.error =
//                    "Password must include one special character !@#\$%^&*+=?\"'(),-./:;<>_`{|}~"
//                return false
//            }
            return true
        }

    }

}