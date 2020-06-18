package vc.rux.codetest.wmp.models

import java.time.LocalDate

data class Customer(
    val customerId: CustomerId,
    val email: String,
    val dateOfBirth: LocalDate,
    val riskLevel: Int,
    val retirementAge: Int
)
