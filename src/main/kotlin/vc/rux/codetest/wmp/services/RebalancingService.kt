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

    fun rebalanceCustomer(customer: Customer): Portfolio {
        val strategy = strategySelector.selectStrategy(customer)
        val customerPortfolio = fpsService.getCustomerPortfolio(customer.customerId)
        return portfolioRebalancer.rebalance(strategy.moneySplit, customerPortfolio)
    }

    fun rebalanceCustomers() {
        val portfoliosToUpdate = customers
            .map { it to rebalanceCustomer(it) }
            .filter { (_, amendPortfolio) -> !amendPortfolio.isEmpty }
            .map { (customer, amendPortfolio) -> customer.customerId to amendPortfolio }
        log.info("rebalanceCustomersL found {} portfolios to be rebalanced: {}",
            portfoliosToUpdate.size, portfoliosToUpdate.joinToString(", ") { "${it.first.value} with ${it.second}"})
        fpsService.executeTrades(portfoliosToUpdate)
    }

    companion object {
        private val log = LoggerFactory.getLogger(RebalancingService::class.java)
    }
}