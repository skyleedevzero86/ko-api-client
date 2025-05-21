package com.koapiclient.dify.config

/**
 * Dify API 설정 클래스
 */
class DifyConfig {

    var apiBaseUrl: String
    var apiKey: String
    var timeout: Int = 30000 // 기본 타임아웃 시간 (밀리초)

    constructor(apiBaseUrl: String, apiKey: String) {
        this.apiBaseUrl = apiBaseUrl
        this.apiKey = apiKey
    }

    // API 기본 URL을 반환합니다.
    fun getApiBaseUrl(): String {
        return apiBaseUrl
    }

    // API 기본 URL을 설정합니다.
    fun setApiBaseUrl(apiBaseUrl: String) {
        this.apiBaseUrl = apiBaseUrl
    }

    // API 키를 반환합니다.
    fun getApiKey(): String {
        return apiKey
    }

    // API 키를 설정합니다.
    fun setApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    // 타임아웃을 반환합니다.
    fun getTimeout(): Int {
        return timeout
    }

    // 타임아웃을 설정합니다.
    fun setTimeout(timeout: Int) {
        this.timeout = timeout
    }
}