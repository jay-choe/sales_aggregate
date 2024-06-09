package com.jay.sales.infrastructure

import com.jay.sales.domain.VendorPrice
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VendorPriceRepository : JpaRepository<VendorPrice, UUID>
