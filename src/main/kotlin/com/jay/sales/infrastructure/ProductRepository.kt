package com.jay.sales.infrastructure

import com.jay.sales.domain.Product
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProductRepository : JpaRepository<Product, UUID> {

}
