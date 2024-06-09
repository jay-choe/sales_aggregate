package com.jay.sales.domain

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.util.UUID

@Entity
class Product(
    @Id
    val id: UUID,
    val name: String
) {

    @OneToMany(mappedBy = "product")
    @BatchSize(size = 10)
    var vendorPrice: Set<VendorPrice> = mutableSetOf()
}