package com.koapiclient.dify.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.koapiclient.dify.exception.DifyApiException
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpEntity
import org.apache.hc.core5.http.HttpStatus
import org.apache.hc.core5.http.ParseException
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.util.Timeout
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpClientUtil {

    private val objectMapper = ObjectMapper()

    @Throws(DifyApiException::class)
    fun <T> doGet(url: String, apiKey: String, timeout: Int, targetClass: Class<T>): T {
        createHttpClient(timeout).use { httpClient ->
            val httpGet = HttpGet(url)
            httpGet.setHeader("Authorization", "Bearer $apiKey")
            httpGet.setHeader("Content-Type", "application/json")

            try {
                httpClient.execute(httpGet).use { response ->
                    return handleResponse(response, targetClass)
                }
            } catch (e: ParseException) {
                throw RuntimeException(e)
            } catch (e: IOException) {
                throw DifyApiException("Failed to execute GET request: ${e.message}", e)
            }
        }
    }

    @Throws(DifyApiException::class)
    fun <T> doPost(url: String, apiKey: String, requestBody: Any, timeout: Int, targetClass: Class<T>): T {
        createHttpClient(timeout).use { httpClient ->
            val httpPost = HttpPost(url)
            httpPost.setHeader("Authorization", "Bearer $apiKey")
            httpPost.setHeader("Content-Type", "application/json")

            val jsonBody = objectMapper.writeValueAsString(requestBody)
            httpPost.entity = StringEntity(jsonBody, ContentType.APPLICATION_JSON)

            try {
                httpClient.execute(httpPost).use { response ->
                    return handleResponse(response, targetClass)
                }
            } catch (e: IOException) {
                throw DifyApiException("Failed to execute POST request: ${e.message}", e)
            } catch (e: ParseException) {
                throw DifyApiException("Failed to parse response: ${e.message}", e)
            }
        }
    }

    private fun createHttpClient(timeout: Int): CloseableHttpClient {
        val requestConfig = RequestConfig.custom()
            .setConnectTimeout(Timeout.of(timeout.toLong(), TimeUnit.MILLISECONDS))
            .setResponseTimeout(Timeout.of(timeout.toLong(), TimeUnit.MILLISECONDS))
            .build()

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .build()
    }

    @Throws(IOException::class, ParseException::class)
    private fun <T> handleResponse(response: CloseableHttpResponse, targetClass: Class<T>): T {
        val statusCode = response.code
        val entity: HttpEntity? = response.entity
        val responseBody = entity?.let { EntityUtils.toString(it) }

        if (statusCode in HttpStatus.SC_SUCCESS until HttpStatus.SC_REDIRECTION) {
            if (targetClass == String::class.java) {
                @Suppress("UNCHECKED_CAST")
                return responseBody as T
            } else {
                return objectMapper.readValue(responseBody, targetClass)
            }
        } else {
            throw DifyApiException("API request failed with status code: $statusCode, response: $responseBody", statusCode)
        }
    }
}