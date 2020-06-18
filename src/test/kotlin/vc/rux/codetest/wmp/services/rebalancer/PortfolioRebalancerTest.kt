package vc.rux.codetest.wmp.services.rebalancer

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Portfolio
import vc.rux.codetest.wmp.services.IPortfolioRebalancer


internal class PortfolioRebalancerTest {

    private lateinit var portfolioRebalancer: IPortfolioRebalancer

    @BeforeEach
    fun init() {
        portfolioRebalancer = PortfolioRebalancer()
    }

    @Test
    fun `no rebalancing need if current portfolio has same assets distribution`() {
        // when
        val portfolio = portfolioRebalancer.rebalance(
            targetMoneySplit = MoneySplit.fromPercent(20, 20, 60),
            currentPortfolio = Portfolio(200, 200, 600)
        )

        // then
        assertThat(portfolio)
            .isEqualTo(Portfolio(0, 0, 0))

        assertTrue(portfolio.isEmpty)
    }


    @Test
    fun `assets need rebalancing`() {
        // when
        val portfolio = portfolioRebalancer.rebalance(
            targetMoneySplit = MoneySplit.fromPercent(20, 30, 50), // target -> (600, 900, 1500)
            currentPortfolio = Portfolio(1000, 1000, 1000)
        )

        // then
        assertThat(portfolio)
            .isEqualTo(Portfolio(-400, -100, 500))
    }

}