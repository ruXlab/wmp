package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.models.StrategyId
import kotlin.Int.Companion.MAX_VALUE

/**
 * This strategy selector is looking for the available
 * strategies for given customer and picks the one with minimum max risk
 */
class StrategySelector(
    private val strategies: List<Strategy>
) : IStrategySelector {

    override fun selectStrategy(customer: Customer): Strategy = strategies
        .filter {
            customer.age in it.yearsToRetirement && customer.riskLevel in it.risk
        }
        .minBy { maxOf(it.risk.first, it.risk.last) } ?: DEFAULT_STRATEGY

    companion object {
        val DEFAULT_STRATEGY = Strategy(
            strategyId = StrategyId.DEFAULT_STRATEGY_ID,
            risk = 0..MAX_VALUE,
            yearsToRetirement = 0..MAX_VALUE,
            moneySplit = MoneySplit(0, 100, 0)
        )
    }
}