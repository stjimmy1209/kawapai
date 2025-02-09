package org.ghayuros

import io.github.oshai.kotlinlogging.KotlinLogging
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


fun main(args: Array<String>) {
    val body = "{\"contents\": [{ \"parts\": [{\"text\": \"${args[0]}\"}] }]}"
    val response = callGemini(body)
    println(response)
}

fun callGemini(input: String): String {
    val logger = KotlinLogging.logger {}
    val apiKey = "AIzaSyBKZc8z-iwiefPT6R28kGTjFj-XXFSFS58"
    val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=${apiKey}"
    val request = Request.Builder()
        .url(url)
        .post(input.toRequestBody("application/json".toMediaType()))
        .build()
    val client = OkHttpClient()

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful){
            logger.error { response.message }
            throw IOException("Unexpected code $response")
        }
        return response.body!!.string()
    }
}