package vc.rux.codetest.wmp.services

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.Portfolio
import vc.rux.codetest.wmp.services.fps.FpsService
import vc.rux.codetest.wmp.services.rebalancer.PortfolioRebalancer
import vc.rux.codetest.wmp.services.strategy.StrategySelector

@Service
class RebalancingService(
    private val customers: List<Customer>,
    private val strategySelector: StrategySelector,
    private val fpsService: FpsService,
    private val portfolioRebalancer: PortfolioRebalancer
) {

    fun rebalanceCustomer(customer: Customer): Portfolio? = try {
        val strategy = strategySelector.selectStrategy(customer)
        val customerPortfolio = fpsService.getCustomerPortfolio(customer.customerId)
        val adjust = portfolioRebalancer.rebalance(strategy.moneySplit, customerPortfolio)
        log.info("rebalanceCustomer #{}: using strategy #{} from current portfolio {} need to adjust with {}",
            customer.customerId.value, strategy.strategyId.strategyId, customerPortfolio, adjust)
        adjust
    } catch (e: Exception) {
        log.error("Error occuried while rebalancing customer {}", customer.customerId.value, e)
        null
    }

    fun rebalanceCustomers() {
        val portfoliosToUpdate = customers
            .mapNotNull { customer -> rebalanceCustomer(customer)?.let { customer.customerId to it } }
            .filter { (_, amendPortfolio) -> !amendPortfolio.isEmpty }
            .map { (customerId, amendPortfolio) -> customerId to amendPortfolio }
        log.info("rebalanceCustomers found {} portfolios to be rebalanced: {}",
            portfoliosToUpdate.size, portfoliosToUpdate.joinToString(", ") { "${it.first.value} with ${it.second}"})
        fpsService.executeTrades(portfoliosToUpdate)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RebalancingService::class.java)
    }
}