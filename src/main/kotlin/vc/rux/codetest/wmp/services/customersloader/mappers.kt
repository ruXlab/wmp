package vc.rux.codetest.wmp.services.customersloader

import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.CustomerId

internal fun CustomerCsvRecord.toModel(): Customer = Customer(
    customerId = CustomerId(customerId),
    email = email,
    dateOfBirth = dateOfBirth,
    riskLevel = riskLevel,
    retirementAge = retirementAge
)

internal fun Iterable<CustomerCsvRecord>.toModels(): List<Customer>
    = this.map(CustomerCsvRecord::toModel)