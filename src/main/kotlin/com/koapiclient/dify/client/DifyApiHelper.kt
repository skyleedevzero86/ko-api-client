package com.koapiclient.dify.client

import com.koapiclient.dify.config.DifyConfig

class DifyApiHelper {

    private val config: DifyConfig

    constructor(config: DifyConfig) {
        this.config = config
    }

    constructor(apiBaseUrl: String, apiKey: String) {
        this.config = DifyConfig(apiBaseUrl, apiKey)
    }

    // 여기에 다양한 API 메서드를 추가할 수 있습니다.
    // 예: sendMessage, createConversation 등
    // 이러한 메서드를 구현하려면 Dify API 문서가 필요합니다.
}