package com.jay.sales.service.command

import java.util.*

data class ProductAddCommand(
    val productId: UUID = UUID.randomUUID(),
    val productName: String
)
