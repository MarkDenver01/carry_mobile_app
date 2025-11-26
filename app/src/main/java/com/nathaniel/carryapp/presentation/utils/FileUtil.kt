package com.nathaniel.carryapp.presentation.utils

import android.content.Context
import android.net.Uri

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Uri.toMultipart(context: Context): MultipartBody.Part {
    val resolver = context.contentResolver
    val mimeType = resolver.getType(this) ?: "image/*"

    val inputStream = resolver.openInputStream(this)
        ?: throw Exception("Cannot open input stream")

    val bytes = inputStream.readBytes()

    // Determine file extension
    val ext = when (mimeType) {
        "image/png" -> "png"
        "image/jpeg", "image/jpg" -> "jpg"
        "image/webp" -> "webp"
        else -> "jpg"
    }

    val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

    return MultipartBody.Part.createFormData(
        "file",
        "customer_${System.currentTimeMillis()}.$ext",
        body
    )
}