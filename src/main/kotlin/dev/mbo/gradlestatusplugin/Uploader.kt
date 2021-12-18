package dev.mbo.gradlestatusplugin

import org.gradle.api.GradleException
import org.slf4j.Logger
import java.net.ConnectException
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Uploader {

    fun upload(url: String, json: String, logger: Logger) {
        logger.info("uploadUrl: {}", url)
        logger.info("json: {}", json)

        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .header("Content-Type", "application/json")
            .build()
        try {
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            val status = response.statusCode()
            if (status < 200 || status > 299) {
                throw GradleException("post failed with status code $status: ${response.body()}")
            }
        } catch (exc: ConnectException) {
            throw GradleException("connection to $url failed", exc)
        }
    }

}