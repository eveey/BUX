package com.evastos.bux.data.util

import com.evastos.bux.data.model.rtf.update.Channel
import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

@Suppress("unused")
class MoshiAdapter {

    @ToJson
    fun subscribeChannelToJson(subscribeChannel: SubscribeChannel): String {
        return subscribeChannel.value
    }

    @FromJson
    fun subscribeChannelToJson(string: String): SubscribeChannel? {
        return SubscribeChannel.TradingProductChannel(string)
    }

    @FromJson
    fun channelFromJson(string: String): Channel {
        Channel.values().find { it.value == string }?.let { channel ->
            return channel
        }
        return Channel.UNKNOWN
    }
}
