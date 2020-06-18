package vc.rux.codetest.wmp.fpsclient

internal data class FpsCustomerPortfolioDto(
    val customerId: Long,
    val stocks: Int,
    val cash: Int,
    val bonds: Int
)