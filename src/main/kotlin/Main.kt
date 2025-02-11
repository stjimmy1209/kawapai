package org.ghayuros

import io.github.oshai.kotlinlogging.KotlinLogging
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


fun main(args: Array<String>) {
    val geminiBody = "{\"contents\": [{ \"parts\": [{\"text\": \"${args[0]}\"}] }]}"
    val localLLMBody = """{
    "model": "deepseek-r1-distill-qwen-14b",
    "messages": [
    { "role": "system", "content": "Always answer in rhymes." },
    { "role": "user", "content": "Introduce yourself." }
    ],
    "temperature": 0.7,
    "max_tokens": 5000,
    "stream": false
    }"""
    val response = callLocalLLM(localLLMBody)
    println(response)
}

fun callGemini(input: String): String {
    val logger = KotlinLogging.logger {}
    val apiKey = "AIzaSyB2VA1IO8Pc_GCmXh88AEgt25v6Pk02C1w"
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

fun callLocalLLM(input: String): String {
    val logger = KotlinLogging.logger {}
    val url = "http://localhost:1234/api/v0/chat/completions"
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