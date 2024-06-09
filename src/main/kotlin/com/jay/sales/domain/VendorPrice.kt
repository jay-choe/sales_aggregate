package com.jay.sales.domain

import jakarta.persistence.*

@Entity
class VendorPrice(
    val vendorName: String,
    val price: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    val product: Product
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
        protected set
}
