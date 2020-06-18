package vc.rux.codetest.wmp.services

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import vc.rux.codetest.wmp.customerGenerator
import vc.rux.codetest.wmp.fpsclient.FpsClient
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.models.StrategyId
import vc.rux.codetest.wmp.services.fps.FpsService
import vc.rux.codetest.wmp.services.rebalancer.PortfolioRebalancer
import vc.rux.codetest.wmp.services.strategy.StrategySelector

@ExtendWith(MockKExtension::class)
internal class RebalancingServiceEndToEndTest {
    @InjectMockKs
    private lateinit var restTemplate: RestTemplate

    @InjectMockKs
    private lateinit var portfolioRebalancer: PortfolioRebalancer

    private lateinit var restServiceServer: MockRestServiceServer

    private lateinit var fpsService: FpsService

    private val targetFakeBaseUrl = "http://localhost"

    @BeforeEach
    fun init() {
        restServiceServer = MockRestServiceServer.createServer(restTemplate)
        fpsService = FpsService(FpsClient(targetFakeBaseUrl, restTemplate), batchSize = 100)
    }

    /**
     * Full end to end test
     * There are two customers:
     *      #1 won't have match with given strategies so default one will be implemented
     *          given assets: "stocks": 1000, "bonds": 1000, "cash": 1000
     *          strategy: 0%, 0%, 100%
     *          target portfolio: 0, 0, 3000
     *          expected update: -1000, -1000, 2000
     *      #2 will match with strategy#13
     *          given assets: "stocks": 500, "bonds": 200, "cash": 400
     *          strategy: 20%, 30%, 50%,
     *          target portfolio: 220, 330, 550,
     *          expected update: -280, 130, 150
     */
    @Test
    fun `full test with two users`() {
        // given inner state
        val customers = listOf(
            customerGenerator(yearsToRetire = 40, riskLevel = 42, email = "ann@mail.com", customerId = 1L), // <- default strategy will be chosen
            customerGenerator(yearsToRetire = 10, riskLevel = 4, email = "bonne@mail.com", customerId = 2L) // <- strategyId#13 will be chosen
        )
        val strategies = listOf(
            Strategy(StrategyId(11), risk = 0..50, yearsToRetirement = 0..10, moneySplit = MoneySplit.fromPercent(10, 20, 70)),
            Strategy(StrategyId(12), risk = 50..60, yearsToRetirement = 0..40, moneySplit = MoneySplit.fromPercent(100, 0, 0)),
            Strategy(StrategyId(13), risk = 2..4, yearsToRetirement = 10..40, moneySplit = MoneySplit.fromPercent(20, 50, 30)), // <- strategy for customerId#2
            Strategy(StrategyId(14), risk = 2..4, yearsToRetirement = 10..10, moneySplit = MoneySplit.fromPercent(10, 80, 10))
        )

        val strategySelector = StrategySelector(strategies)

        val service = RebalancingService(customers, strategySelector, fpsService, portfolioRebalancer)

        // given server state
        restServiceServer
            .expect(requestTo("$targetFakeBaseUrl/customer/1"))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""{ "customerId": 1, "stocks": 1000, "bonds": 1000, "cash": 1000}""")   // sum = 3000
            )
        restServiceServer
            .expect(requestTo("$targetFakeBaseUrl/customer/2"))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("""{ "customerId": 2, "stocks": 500, "bonds": 200, "cash": 400}""") // sum = 1100
            )

        restServiceServer
            .expect { requestTo("$targetFakeBaseUrl/execute") }
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().json("""[
                | { "customerId": 1, "stocks": -1000, "bonds": -1000, "cash": 2000 },
                | { "customerId": 2, "stocks": -280, "bonds": 130, "cash": 150 }
            ]""".trimMargin()))
            .andRespond(
                withStatus(HttpStatus.CREATED)
            )

        // when
        service.rebalanceCustomers()

    }

}