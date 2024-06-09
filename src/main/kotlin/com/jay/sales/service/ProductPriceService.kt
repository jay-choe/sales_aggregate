package com.jay.sales.service

import com.jay.sales.infrastructure.ProductRepository
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import com.jay.sales.service.response.ProductPrice
import com.jay.sales.service.response.ProductPriceInfos
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductPriceService(
    private val productRepository: ProductRepository,
) {

    @Transactional(readOnly = true)
    @Cacheable("min-price-vendors")
    fun findLowestPriceVendor(query: TopNLowPriceVendorsQuery): ProductPriceInfos {
        val lowestPriceVendorName =
            productRepository.findLowestPriceProductsWithProductId(query.productId)
                .flatMap { it.vendorPrice }
                .map { ProductPrice(it.vendorName, it.price) }

        return ProductPriceInfos(lowestPriceVendorName)
    }
}
