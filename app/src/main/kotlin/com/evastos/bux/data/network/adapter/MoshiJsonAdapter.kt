package com.evastos.bux.data.network.adapter

import com.evastos.bux.data.model.rtf.subscription.SubscribeChannel
import com.evastos.bux.data.model.rtf.update.Channel
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

@Suppress("unused")
class MoshiJsonAdapter {

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

    @ToJson
    fun bigDecimalToJson(bigDecimal: BigDecimal): String {
        return bigDecimal.toPlainString()
    }

    @FromJson
    fun bigDecimalFromJson(string: String): BigDecimal {
        return string.toBigDecimal()
    }
}
