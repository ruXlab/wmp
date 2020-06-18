package vc.rux.codetest.wmp.services

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import vc.rux.codetest.wmp.customerGenerator
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.models.StrategyId
import vc.rux.codetest.wmp.services.strategy.StrategySelector

internal class StrategySelectorTest {
    private val defaultMoneySplit = MoneySplit.fromPercent(0, 50, 50) // doesn't matter for this test, could be rather mocked

    @Test
    fun `test no given strategies match - expected default one to be returned`() {
        // given
        val strategySelector = StrategySelector(listOf())

        // when
        val strategy = strategySelector.selectStrategy(
            customerGenerator(42, 42)
        )

        // then
        assertThat(strategy.strategyId)
            .isEqualTo(StrategyId.DEFAULT_STRATEGY_ID)
    }

    @Test
    fun `when the only strategy matches profile it will be chosen`() {
        // given
        val strategySelector = StrategySelector(listOf(
            Strategy(StrategyId(1), risk = 0..50, yearsToRetirement = 0..10, moneySplit = defaultMoneySplit),
            Strategy(StrategyId(123), risk = 0..50, yearsToRetirement = 20..30, moneySplit = defaultMoneySplit),
            Strategy(StrategyId(2), risk = 0..50, yearsToRetirement = 30..100, moneySplit = defaultMoneySplit)
        ))

        // when
        val strategy = strategySelector.selectStrategy(
            customerGenerator(42, 42)
        )

        // then
        assertThat(strategy.strategyId)
            .isNotEqualTo(StrategyId.DEFAULT_STRATEGY_ID)
            .isEqualTo(StrategyId(123))
    }

    @Test
    fun `when the multiple strategies matches profile the least risky will be chosen`() {
        // given
        val strategySelector = StrategySelector(listOf(
            Strategy(StrategyId(1), risk = 0..50, yearsToRetirement = 0..10, moneySplit = defaultMoneySplit),
            Strategy(StrategyId(1001), risk = 0..50, yearsToRetirement = 20..30, moneySplit = defaultMoneySplit),
            Strategy(StrategyId(1002), risk = 0..30, yearsToRetirement = 20..30, moneySplit = defaultMoneySplit), // <- to be selected
            Strategy(StrategyId(2), risk = 0..50, yearsToRetirement = 30..100, moneySplit = defaultMoneySplit)
        ))

        // when
        val strategy = strategySelector.selectStrategy(
            customerGenerator(yearsToRetire = 42, riskLevel = 11)
        )

        // then
        assertThat(strategy.strategyId)
            .isNotEqualTo(StrategyId.DEFAULT_STRATEGY_ID)
            .isEqualTo(StrategyId(1002))
    }



}