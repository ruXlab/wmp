package vc.rux.codetest.wmp.services

import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import vc.rux.codetest.wmp.fpsclient.IFpsClient
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
internal class FpsServiceTest {
    @MockK
    lateinit var fpsClient: IFpsClient

    @InjectMockKs
    lateinit var trades: ArrayList<List<Pair<CustomerId, Portfolio>>>

    @Test
    fun `service chunks trade requests`() {
        // given
        every { fpsClient.executeTrades(capture(trades)) } just Runs

        val inputTrades = listOf(
            CustomerId(1) to randomPortfolio(),
            CustomerId(2) to randomPortfolio(),
            CustomerId(3) to randomPortfolio()
        )

        val fpsService = FpsService(fpsClient, 2)

        // when
        fpsService.executeTrades(inputTrades)

        // then
        verify(exactly = 2) { fpsClient.executeTrades(any()) }

        assertThat(trades)
            .hasSize(2)

        assertThat(trades.flatten().map { it.first.value })
            .containsExactly(1, 2, 3)
    }

    private fun randomPortfolio() = Portfolio(Random.nextInt(), Random.nextInt(), Random.nextInt())

}