package com.jay.sales.service

import com.jay.sales.domain.VendorPrice
import com.jay.sales.infrastructure.ProductRepository
import com.jay.sales.infrastructure.VendorPriceRepository
import com.jay.sales.service.command.VendorPriceAddCommand
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class VendorPriceService(
    private val productRepository: ProductRepository,
    private val vendorPriceRepository: VendorPriceRepository
) {

    @Transactional
    fun add(vendorPriceAddCommand: VendorPriceAddCommand) {
        val vendorPriceInfo = vendorPriceAddCommand.let { VendorPrice(
            vendorName = it.vendorName,
            price = it.price,
            product = productRepository.findById(it.productId).orElseThrow()) }

        vendorPriceRepository.save(vendorPriceInfo)
    }
}
