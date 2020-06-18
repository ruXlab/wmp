package vc.rux.codetest.wmp.services.strategiesloader

data class StrategyCsvRecord(
    val strategyId: Long,
    val minRiskLevel: Int,
    val maxRiskLevel: Int,
    val minYearsToRetirement: Int,
    val maxYearsToRetirement: Int,
    val stocksPercentage: Int,
    val cashPercentage: Int,
    val bondsPercentage: Int
) {
}