package vc.rux.codetest.wmp.services.fps

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import vc.rux.codetest.wmp.fpsclient.IFpsClient
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio
import vc.rux.codetest.wmp.services.IFpsService

@Service
@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
class FpsService(
    private val fpsClient: IFpsClient,
    @Value("\${fps.batchsize}")
    private val batchSize: Int
) : IFpsService {
    override fun getCustomerPortfolio(customerId: CustomerId): Portfolio {
        return fpsClient.getCustomerPortfolio(customerId)
    }

    override fun executeTrades(trades: List<Pair<CustomerId, Portfolio>>) {
        if (trades.isEmpty()) return
        trades.chunked(batchSize.toInt()).forEach(fpsClient::executeTrades)
    }
}