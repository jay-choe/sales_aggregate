package com.jay.sales.service

import com.jay.sales.domain.Product
import com.jay.sales.infrastructure.ProductRepository
import com.jay.sales.service.command.ProductAddCommand
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductInfoService(
    private val productRepository: ProductRepository
) {

    @Transactional
    fun addProduct(productAddCommand: ProductAddCommand) {
        productAddCommand.apply {
            productRepository.save(Product(this.productId, this.productName))
        }
    }
}
