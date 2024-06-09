package com.jay.sales.service.response

import java.io.Serializable

data class ProductPriceInfos(
    val productPriceInfoList: List<ProductPrice>
) : Serializable {

    fun hasSize(): Boolean = productPriceInfoList.isNotEmpty()
}

data class ProductPrice(
    val vendor: String,
    val price: Long
) : Serializable
