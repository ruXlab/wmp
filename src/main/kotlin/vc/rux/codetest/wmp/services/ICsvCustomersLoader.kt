package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.Customer
import java.io.InputStream

interface ICsvCustomersLoader {
    fun loadCustomers(inputStream: InputStream): List<Customer>
}


