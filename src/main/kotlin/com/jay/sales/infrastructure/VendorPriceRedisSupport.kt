package com.jay.sales.infrastructure

import com.jay.sales.service.command.VendorPriceAddCommand
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import com.jay.sales.service.response.ProductPrice
import com.jay.sales.service.response.ProductPriceInfos
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class VendorPriceRedisSupport(
    val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val VERSION_PREFIX = "V1"
        const val PRODUCT_PRICE_PREFIX = "$VERSION_PREFIX-product-%s"
    }


    fun saveProductVendorPrice(command: VendorPriceAddCommand) {
        redisTemplate.opsForZSet().add(PRODUCT_PRICE_PREFIX.format(command.productId), command.vendorName, command.price.toDouble())
        // TODO: TTL 적용.. 고민해보기 + 분산환경 값 보장하는 방법
    }

    fun getTopNLowestPrice(query: TopNLowPriceVendorsQuery): ProductPriceInfos {
        val key = PRODUCT_PRICE_PREFIX.format(query.productId)

        val rangeWithScores = redisTemplate.opsForZSet().rangeWithScores(key, 0, query.topN.toLong())

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
