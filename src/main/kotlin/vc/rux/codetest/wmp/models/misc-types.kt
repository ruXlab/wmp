package vc.rux.codetest.wmp.models

inline class CustomerId(val value: Long)

inline class StrategyId(val strategyId: Long) {
    companion object {
        val DEFAULT_STRATEGY_ID = StrategyId(-1)
    }
}