package vc.rux.codetest.wmp.models

data class Strategy(
    val strategyId: StrategyId,
    val risk: IntRange,
    val yearsToRetirement: IntRange,
    val moneySplit: MoneySplit
) {
}