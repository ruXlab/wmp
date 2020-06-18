package vc.rux.codetest.wmp.models

data class Portfolio(
    val stocks: Int,
    val cash: Int,
    val bonds: Int
) {
    val isEmpty: Boolean
        get() = stocks == 0 && cash == 0 && bonds == 0

    fun sum(): Int = stocks + cash + bonds

    fun ratio(): MoneySplit {
        val currentSum = sum().toDouble()

        return MoneySplit(
            cash = cash / currentSum,
            stocks = stocks / currentSum,
            bonds = bonds / currentSum
        )
    }

    operator fun minus(other: Portfolio): Portfolio = Portfolio(
        stocks - other.stocks, cash - other.cash, bonds - other.bonds
    )
}