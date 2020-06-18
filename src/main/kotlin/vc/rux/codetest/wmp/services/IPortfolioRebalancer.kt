package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Portfolio

interface IPortfolioRebalancer {
    fun rebalance(targetMoneySplit: MoneySplit, currentPortfolio: Portfolio): Portfolio
}