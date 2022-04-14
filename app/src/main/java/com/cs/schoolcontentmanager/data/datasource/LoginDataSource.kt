package com.cs.schoolcontentmanager.data.datasource

import com.cs.schoolcontentmanager.data.Result
import com.cs.schoolcontentmanager.domain.model.login.LoggedInUser
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        return try {
            auth.signInWithEmailAndPassword(username, password).await()
            withContext(Dispatchers.Main) {
                auth.currentUser?.let {
                    it.email?.let { email_ -> LoggedInUser(it.uid, email_) }
                }
            }
            val user = auth.currentUser
            if (user != null) Result.Success(LoggedInUser(user.uid, user.email.toString()))
            else Result.Error(Exception("Error logging in"))
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        auth.signOut()
    }
}