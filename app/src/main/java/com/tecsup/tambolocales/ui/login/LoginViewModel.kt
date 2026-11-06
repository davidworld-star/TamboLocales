package com.tecsup.tambolocales.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    sealed class LoginResult {
        object Success : LoginResult()
        data class Error(val messageResId: Int) : LoginResult()
    }

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult

    fun login(email: String, clave: String) {
        val trimmedEmail = email.trim()
        val trimmedClave = clave.trim()

        if (trimmedEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            _loginResult.value = LoginResult.Error(com.tecsup.tambolocales.R.string.login_error_email_format)
            return
        }

        if (trimmedClave.isEmpty()) {
            _loginResult.value = LoginResult.Error(com.tecsup.tambolocales.R.string.login_error_password_empty)
            return
        }

        if (trimmedEmail == "admin@tambo.com" && trimmedClave == "Tambo123") {
            _loginResult.value = LoginResult.Success
        } else {
            _loginResult.value = LoginResult.Error(com.tecsup.tambolocales.R.string.login_error_credentials)
        }
    }

    fun clearResult() {
        _loginResult.value = null
    }
}
