package vc.rux.codetest.wmp.services

import vc.rux.codetest.wmp.models.Strategy
import java.io.InputStream

interface ICsvStrategyLoader {
    fun loadStrategy(inputStream: InputStream): List<Strategy>
}


