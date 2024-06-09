package com.jay.sales.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
class Product(
    @Id
    val id: UUID,
    val name: String
) {
    @OneToMany(mappedBy = "product")
    var vendorPrice: Set<VendorPrice> = mutableSetOf()
}