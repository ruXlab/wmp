package vc.rux.codetest.wmp.fpsclient

import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio

internal fun FpsCustomerPortfolioDto.toModel(): Portfolio = Portfolio(
    stocks = stocks,
    cash = cash,
    bonds = bonds
)

internal fun Pair<CustomerId, Portfolio>.toDto(): FpsCustomerPortfolioDto = FpsCustomerPortfolioDto(
    customerId = first.value,
    bonds = second.bonds,
    cash = second.cash,
    stocks = second.stocks
)

internal fun Iterable<Pair<CustomerId, Portfolio>>.toDto(): List<FpsCustomerPortfolioDto>
    = this.map(Pair<CustomerId, Portfolio>::toDto)