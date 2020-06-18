package vc.rux.codetest.wmp

import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.CustomerId
import java.time.LocalDate
import kotlin.math.absoluteValue
import kotlin.random.Random

internal fun customerGenerator(
    yearsToRetire: Int,
    riskLevel: Int,
    customerId: Long = Random.nextLong().absoluteValue,
    email: String = "customer${System.currentTimeMillis()}@email.com"
) = Customer(
    customerId = CustomerId(customerId),
    riskLevel = riskLevel,
    email = email,
    retirementAge = yearsToRetire,
    dateOfBirth = LocalDate.of(2000, 1, 1)
)
