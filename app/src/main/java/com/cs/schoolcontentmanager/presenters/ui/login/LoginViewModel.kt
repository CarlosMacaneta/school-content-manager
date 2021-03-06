package com.cs.schoolcontentmanager.presenters.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.schoolcontentmanager.R
import com.cs.schoolcontentmanager.data.Result
import com.cs.schoolcontentmanager.data.repository.LoginRepository
import com.cs.schoolcontentmanager.domain.model.login.LoggedInUserView
import com.cs.schoolcontentmanager.domain.model.login.LoginFormState
import com.cs.schoolcontentmanager.domain.model.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject  constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    suspend fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            withContext(Dispatchers.Main) {
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }
        } else {
            withContext(Dispatchers.Main) {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}