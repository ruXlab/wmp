package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio

interface IFpsService {
    fun getCustomerPortfolio(customerId: CustomerId): Portfolio
    fun executeTrades(trades: List<Pair<CustomerId, Portfolio>>)
}