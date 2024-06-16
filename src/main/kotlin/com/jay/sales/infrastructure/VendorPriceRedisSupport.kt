package com.jay.sales.infrastructure

import com.jay.sales.service.command.VendorPriceAddCommand
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import com.jay.sales.service.response.ProductPrice
import com.jay.sales.service.response.ProductPriceInfos
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration.*

@Component
class VendorPriceRedisSupport(
    val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val VERSION_PREFIX = "V1"
        const val PRODUCT_PRICE_KEY_FORMAT = "$VERSION_PREFIX-%s"
    }

    fun renewProductVendorPrice(command: VendorPriceAddCommand) {
        val key = PRODUCT_PRICE_KEY_FORMAT.format(command.productId)

        redisTemplate.execute { connection ->
            connection.openPipeline()
            val zSetOperation = redisTemplate.opsForZSet()

            zSetOperation.add(key, command.vendorName, command.price.toDouble())
            connection.keyCommands().expire(key.toByteArray(), ofDays(1).toSeconds())
            connection.closePipeline()
        }
    }

    fun getTopNLowestPrice(query: TopNLowPriceVendorsQuery): ProductPriceInfos {
        val key = PRODUCT_PRICE_KEY_FORMAT.format(query.productId)

        val rangeWithScores =
            redisTemplate.opsForZSet().rangeWithScores(key, 0, query.topN.toLong())

        val productPrices = rangeWithScores?.mapNotNull { tuple ->
            tuple.value?.let { value ->
                tuple.score?.let { score ->
                    ProductPrice(value, score.toLong())
                }
            }
        } ?: emptyList()

        return ProductPriceInfos(productPrices)
    }

}
