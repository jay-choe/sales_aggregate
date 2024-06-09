package com.jay.sales.service.command

import java.util.UUID

data class VendorPriceAddCommand(
    val productId: UUID,
    val vendorName: String,
    val price: Long
)
