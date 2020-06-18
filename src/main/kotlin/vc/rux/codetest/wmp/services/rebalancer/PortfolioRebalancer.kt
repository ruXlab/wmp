package vc.rux.codetest.wmp.services.rebalancer

import org.springframework.stereotype.Service
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Portfolio
import vc.rux.codetest.wmp.services.IPortfolioRebalancer

@Service
class PortfolioRebalancer : IPortfolioRebalancer {
    override fun rebalance(targetMoneySplit: MoneySplit, currentPortfolio: Portfolio): Portfolio {
        val currentPortfolioSum = currentPortfolio.sum()

        val targetPortfolio = Portfolio(
            stocks = (currentPortfolioSum * targetMoneySplit.stocks).toInt(),
            bonds = (currentPortfolioSum * targetMoneySplit.bonds).toInt(),
            cash = (currentPortfolioSum * targetMoneySplit.cash).toInt()
        )

        return targetPortfolio - currentPortfolio
    }
}