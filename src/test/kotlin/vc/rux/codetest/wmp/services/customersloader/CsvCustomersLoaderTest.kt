package vc.rux.codetest.wmp.services.customersloader

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.services.ICsvCustomersLoader
import vc.rux.codetest.wmp.services.customersloader.CsvCustomersLoader
import java.time.LocalDate

internal class CsvCustomersLoaderTest {

    private lateinit var service: ICsvCustomersLoader

    @BeforeEach
    fun init() {
        service = CsvCustomersLoader()
    }

    @Test
    fun `can parse input given in example`() {
        // when
        val customers = service.loadCustomers(inputGivenInExample.byteInputStream())

        // then
        assertThat(customers)
            .hasSize(2)

        assertThat(customers.firstOrNull { it.customerId == CustomerId(1L) })
            .isEqualTo(
                Customer(customerId = CustomerId(1),
                    email = "bob@bob.com",
                    dateOfBirth = LocalDate.of(1961, 4, 29),
                    riskLevel = 3,
                    retirementAge = 65
                )
            )

        assertThat(customers.firstOrNull { it.customerId == CustomerId(2L) })
            .isEqualTo(
                Customer(customerId = CustomerId(2),
                    email = "sally@gmail.com",
                    dateOfBirth = LocalDate.of(1978, 5, 1),
                    riskLevel = 8,
                    retirementAge = 67
                )
            )
    }


    @Test
    fun `no data if input contains only headers`() {
        // when
        val customers = service.loadCustomers(inputNoRowsOnlyHeader.byteInputStream())

        // then
        assertThat(customers)
            .isEmpty()
    }


    @Test
    fun `error is thrown when there is no data`() {
        // when & then
        assertThrows<java.lang.Exception> {
            service.loadCustomers(inputNoHeadersNoData.byteInputStream())
        }

    }

    @Test
    fun `if malformed data is given exception is thrown`() {
        // when & then
        assertThrows<Exception> {
            service.loadCustomers(inputMalformedData.byteInputStream())
        }
    }



    private val inputGivenInExample = """
        customerId,email,dateOfBirth,riskLevel,retirementAge
        1,bob@bob.com,1961-04-29,3,65
        2,sally@gmail.com,1978-05-01,8,67
    """.trimIndent()

    private val inputNoRowsOnlyHeader = "customerId,email,dateOfBirth,riskLevel,retirementAge\n"

    private val inputNoHeadersNoData = "\n"

    private val inputMalformedData = """
        customerId,email,dateOfBirth,riskLevel,retirementAge
        ONE,bob@bob.com,19610429,3,65
    """.trimIndent()


}