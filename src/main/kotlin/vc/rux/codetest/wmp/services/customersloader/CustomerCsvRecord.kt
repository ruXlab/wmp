package vc.rux.codetest.wmp.services.customersloader

import java.time.LocalDate

/**
 * Internal CSV row as it
 */
internal data class CustomerCsvRecord(
    val customerId: Long,
    val email: String,
    val dateOfBirth: LocalDate,
    val riskLevel: Int,
    val retirementAge: Int
)
