package com.jay.sales.service

import com.jay.sales.service.command.ProductAddCommand
import com.jay.sales.service.command.VendorPriceAddCommand
import com.jay.sales.service.query.TopNLowPriceVendorsQuery
import org.assertj.core.api.Assertions

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class ProductPriceServiceTest {

    @Autowired
    lateinit var productInfoService: ProductInfoService
    @Autowired
    lateinit var productPriceService: ProductPriceService
    @Autowired
    lateinit var vendorPriceService: VendorPriceService

    val productId: UUID = UUID.randomUUID()

    @BeforeEach
    fun setUp() {
        // create product
        val productAddCommand = ProductAddCommand(productId, "TestProduct")
        productInfoService.addProduct(productAddCommand)

        val vendorPriceCommand1 = VendorPriceAddCommand(productId, "TestVendor1", 10000000L)
        val vendorPriceCommand2 = VendorPriceAddCommand(productId, "TestVendor2", 100000L)

        vendorPriceService.add(vendorPriceCommand1)
        vendorPriceService.add(vendorPriceCommand2)
    }

    @Test
    fun findTopNLowPriceProducts() {
        val topNLowPriceProducts =
            productPriceService.findTopNLowPriceProducts(TopNLowPriceVendorsQuery(productId, 1)).productPriceInfoList

        Assertions.assertThat(topNLowPriceProducts).size().isEqualTo(1)
        Assertions.assertThat(topNLowPriceProducts.get(0).price).isEqualTo(100000L)
    }
}