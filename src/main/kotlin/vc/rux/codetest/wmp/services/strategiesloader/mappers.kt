package vc.rux.codetest.wmp.services.strategiesloader

import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.models.StrategyId


internal fun StrategyCsvRecord.toModel(): Strategy = Strategy(
    strategyId = StrategyId(strategyId),
    risk = IntRange(minRiskLevel, maxRiskLevel),
    yearsToRetirement = IntRange(minYearsToRetirement, maxYearsToRetirement),
    moneySplit = MoneySplit.fromPercent(stocksPercentage, cashPercentage, bondsPercentage)
)

internal fun Iterable<StrategyCsvRecord>.toModels(): List<Strategy>
    = this.map(StrategyCsvRecord::toModel)