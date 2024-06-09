package com.jay.sales.service.response

import java.io.Serializable

data class ProductLowestPriceVendors(
    val vendorNames: List<String>
) : Serializable
