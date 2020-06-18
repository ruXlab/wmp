package vc.rux.codetest.wmp.models

data class MoneySplit(
    val stocks: Double,
    val cash: Double,
    val bonds: Double
) {
    companion object {
        fun fromPercent(stocksPercent: Int, cashPercent: Int, bondsPercent: Int): MoneySplit {
            return MoneySplit(stocksPercent / 100.0, cashPercent / 100.0, bondsPercent / 100.0)
        }
    }
}