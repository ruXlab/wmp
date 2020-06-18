package vc.rux.codetest.wmp.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class CustomerTest {
    @Test
    fun `compute age`() {
        // given
        val today = LocalDate.of(2020, 6, 18)
        val customer = Customer(
            customerId = CustomerId(1),
            email = "bob@bob.com",
            dateOfBirth = LocalDate.of(1961, 4, 29),
            riskLevel = 3,
            retirementAge = 65
        )

        // when
        val age = customer.age(today)
        
        // then
        assertThat(age)
            .isEqualTo(59)
    }

    @Test
    fun `compute years to retire`() {
        // given
        val today = LocalDate.of(2020, 6, 18)
        val customer = Customer(
            customerId = CustomerId(1),
            email = "bob@bob.com",
            dateOfBirth = LocalDate.of(1961, 4, 29),
            riskLevel = 3,
            retirementAge = 65
        )

        // when
        val yearsToRetire = customer.yearsToRetire(today)

        // then
        assertThat(yearsToRetire)
            .isEqualTo(6)
    }

}