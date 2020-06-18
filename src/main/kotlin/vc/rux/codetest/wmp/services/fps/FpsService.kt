package vc.rux.codetest.wmp.services.fps

import vc.rux.codetest.wmp.fpsclient.IFpsClient
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio
import vc.rux.codetest.wmp.services.IFpsService

class FpsService(
    private val fpsClient: IFpsClient,
    private val batchSize: Int
) : IFpsService {
    override fun getCustomerPortfolio(customerId: CustomerId): Portfolio {
        return fpsClient.getCustomerPortfolio(customerId)
    }

    override fun executeTrades(trades: List<Pair<CustomerId, Portfolio>>) {
        trades.chunked(batchSize).forEach(fpsClient::executeTrades)
    }
}