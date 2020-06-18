package vc.rux.codetest.wmp.models

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PortfolioTest {
    @Test
    fun `test ratio`() {
        // given
        val portfolio = Portfolio(stocks = 100, cash = 200, bonds = 500) // distribution: 0.125, 0.25, 0.625

        // when
        val moneySplit = portfolio.ratio()

        // then
        assertThat(moneySplit)
            .isEqualTo(MoneySplit(stocks = 0.125, cash = 0.25, bonds = 0.625))
    }

    @Test
    fun `test isEmpty true when all assets are 0`() {
        // given
        val portfolio = Portfolio(0, 0, 0)

        // then
        assertThat(portfolio.isEmpty)
            .isTrue()
    }

    @Test
    fun `test isEmpty false when any assets is not 0`() {
        // given
        val portfolio = Portfolio(0, 1, 0)

        // then
        assertThat(portfolio.isEmpty)
            .isFalse()
    }


    @Test
    fun `test sum works`() {
        // given
        val portfolio = Portfolio(16, 32, 64)

        // then
        assertThat(portfolio.sum())
            .isEqualTo(112)
    }

}