package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.Strategy

interface IStrategySelector {
    fun selectStrategy(customer: Customer): Strategy
}