package com.jay.sales.infrastructure

import com.jay.sales.domain.Product
import com.jay.sales.domain.VendorPrice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface VendorPriceRepository : JpaRepository<VendorPrice, UUID> {

    @Query(
        """
       SELECT v FROM VendorPrice v JOIN FETCH v.product p
       WHERE v.product.id = :productId
       order by v.price
       """
    )
    fun findTopNLowPriceProducts(
        @Param("productId") productId: UUID, pageable: Pageable
    ): Page<VendorPrice>
}
