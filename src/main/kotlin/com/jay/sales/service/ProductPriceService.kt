package com.jay.sales.service

import com.jay.sales.infrastructure.VendorPriceRedisSupport
import com.jay.sales.infrastructure.VendorPriceRepository
import com.jay.sales.service.command.VendorPriceAddCommand
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import com.jay.sales.service.response.ProductPrice
import com.jay.sales.service.response.ProductPriceInfos
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductPriceService(
    private val vendorPriceRepository: VendorPriceRepository,
    private val cacheSupport: VendorPriceRedisSupport
) {

    @Transactional(readOnly = true)
    fun findTopNLowPriceProducts(query: TopNLowPriceVendorsQuery): ProductPriceInfos {

        if (cacheSupport.getTopNLowestPrice(query).hasSize()) {
            return cacheSupport.getTopNLowestPrice(query)
        }

        val topNLowestPriceProducts =
            vendorPriceRepository.findTopNLowPriceProducts(query.productId, PageRequest.of(0, query.topN))
                .content

        // save cache
        topNLowestPriceProducts.map { VendorPriceAddCommand(it.product.id, it.vendorName, it.price) }
            .forEach { cacheSupport.renewProductVendorPrice(it) }

        return ProductPriceInfos(topNLowestPriceProducts.map { ProductPrice(it.vendorName, it.price) })
    }
}
