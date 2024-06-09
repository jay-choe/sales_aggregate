package com.jay.sales.infrastructure

import com.jay.sales.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {

    @Query(
        """
       SELECT p FROM Product p JOIN FETCH p.vendorPrice v WHERE p.id = :productId AND v.price = (SELECT MIN(v2.price) FROM VendorPrice v2 WHERE v2.product.id = :productId)
       """
    )
    fun findLowestPriceProductsWithProductId(productId: UUID): List<Product>
}
