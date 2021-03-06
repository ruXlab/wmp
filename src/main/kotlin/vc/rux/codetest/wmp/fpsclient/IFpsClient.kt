package vc.rux.codetest.wmp.fpsclient

import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio

interface IFpsClient {
    fun getCustomerPortfolio(customerId: CustomerId): Portfolio
    fun executeTrades(trades: List<Pair<CustomerId, Portfolio>>)
}