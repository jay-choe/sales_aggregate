package com.jay.sales.service

import com.jay.sales.infrastructure.ProductRepository
import com.jay.sales.service.query.LowestPriceProductQuery
import com.jay.sales.service.response.ProductLowestPriceVendors
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductPriceService(
    private val productRepository: ProductRepository,
) {

    @Transactional(readOnly = true)
    @Cacheable("lowestPriceVendors")
    fun findLowestPriceVendor(query: LowestPriceProductQuery): ProductLowestPriceVendors {
        val lowestPriceVendorName = productRepository.findLowestPriceProductsWithProductId(query.productId)
            .flatMap { it.vendorPrice }
            .map { it.vendorName }

        return ProductLowestPriceVendors(lowestPriceVendorName)
    }
}
