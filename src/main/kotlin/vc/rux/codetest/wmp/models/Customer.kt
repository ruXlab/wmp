package vc.rux.codetest.wmp.models

import java.time.LocalDate
import java.time.temporal.ChronoUnit.YEARS

data class Customer(
    val customerId: CustomerId,
    val email: String,
    val dateOfBirth: LocalDate,
    val riskLevel: Int,
    val retirementAge: Int
) {
    fun age(toDate: LocalDate = LocalDate.now()): Long
        = YEARS.between(dateOfBirth, toDate)

    fun yearsToRetire(today: LocalDate): Long
        = retirementAge - age(today)
}
