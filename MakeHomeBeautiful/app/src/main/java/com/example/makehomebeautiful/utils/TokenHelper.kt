package com.example.makehomebeautiful.utils

import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object TokenHelper {
    suspend fun getToken(): String? {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)
                ?.addOnSuccessListener { result ->
                    continuation.resume(result.token)
                }?.addOnFailureListener {
                    continuation.resume(null)
                } ?: continuation.resume(null)
        }
    }
}