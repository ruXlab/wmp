package vc.rux.codetest.wmp.services.strategiesloader

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.models.StrategyId
import vc.rux.codetest.wmp.services.ICsvStrategyLoader

internal class CsvStrategiesLoaderTest {

    private lateinit var service: ICsvStrategyLoader

    @BeforeEach
    fun init() {
        service = CsvStrategiesLoader()
    }

    @Test
    fun `can parse input given in example`() {
        // when
        val customers = service.loadStrategy(inputGivenInExample.byteInputStream())

        // then
        assertThat(customers)
            .hasSize(3)

        assertThat(customers.firstOrNull { it.strategyId == StrategyId(1L) })
            .isEqualTo(
                Strategy(strategyId = StrategyId(1),
                    risk = IntRange(0, 3),
                    yearsToRetirement = IntRange(20, 30),
                    moneySplit = MoneySplit.fromPercent(20, 20, 60)
                )
            )

        assertThat(customers.firstOrNull { it.strategyId == StrategyId(2L) })
            .isEqualTo(
                Strategy(strategyId = StrategyId(2),
                    risk = IntRange(0, 3),
                    yearsToRetirement = IntRange(10, 20),
                    moneySplit = MoneySplit.fromPercent(10, 20, 70)
                )
            )

        assertThat(customers.firstOrNull { it.strategyId == StrategyId(3L) })
            .isEqualTo(
                Strategy(strategyId = StrategyId(3),
                    risk = IntRange(6, 9),
                    yearsToRetirement = IntRange(20, 30),
                    moneySplit = MoneySplit.fromPercent(10, 0, 90)
                )
            )
    }


    @Test
    fun `no data if input contains only headers`() {
        // when
        val customers = service.loadStrategy(inputNoRowsOnlyHeader.byteInputStream())

        // then
        assertThat(customers)
            .isEmpty()
    }


    @Test
    fun `error is thrown when there is no data`() {
        // when & then
        assertThrows<java.lang.Exception> {
            service.loadStrategy(inputNoHeadersNoData.byteInputStream())
        }

    }

    @Test
    fun `if malformed data is given exception is thrown`() {
        // when & then
        assertThrows<Exception> {
            service.loadStrategy(inputMalformedData.byteInputStream())
        }
    }


    private val inputGivenInExample = """
        strategyId,minRiskLevel,maxRiskLevel,minYearsToRetirement,maxYearsToRetirement,stocksPercentage,cashPercentage,bondsPercentage
        1,0,3,20,30,20,20,60
        2,0,3,10,20,10,20,70
        3,6,9,20,30,10,0,90
    """.trimIndent()

    private val inputNoRowsOnlyHeader = "strategyId,minRiskLevel,maxRiskLevel,minYearsToRetirement,maxYearsToRetirement,stocksPercentage,cashPercentage,bondsPercentage\n"

    private val inputNoHeadersNoData = "\n"

    private val inputMalformedData = """
        strategyId,minRiskLevel,maxRiskLevel,minYearsToRetirement,maxYearsToRetirement,stocksPercentage,cashPercentage,bondsPercentage
        TWO,0,3,10,20,10,20,70
    """.trimIndent()



}