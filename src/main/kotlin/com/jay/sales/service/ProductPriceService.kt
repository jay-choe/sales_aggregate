package com.jay.sales.service

import com.jay.sales.infrastructure.ProductRepository
import com.jay.sales.infrastructure.VendorPriceRepository
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import com.jay.sales.service.response.ProductPrice
import com.jay.sales.service.response.ProductPriceInfos
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductPriceService(
    private val productRepository: ProductRepository,
    private val vendorPriceRepository: VendorPriceRepository
) {

    @Transactional(readOnly = true)
    @Cacheable("min-price-products")
    fun findTopNLowPriceProducts(query: TopNLowPriceVendorsQuery): ProductPriceInfos {
        val lowestPriceVendorName =
            vendorPriceRepository.findTopNLowPriceProducts(query.productId, PageRequest.of(0, query.topN))
                .content
                .map { ProductPrice(it.vendorName, it.price) }

        return ProductPriceInfos(lowestPriceVendorName)
    }
}
