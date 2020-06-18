package vc.rux.codetest.wmp.services.strategiesloader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.services.ICsvStrategyLoader
import java.io.InputStream

@Service
class CsvStrategiesLoader : ICsvStrategyLoader {
    private val csvReader by lazy {
        val schema = CsvSchema.emptySchema().withHeader()
        val csvMapper = CsvMapper()
            .configure(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
            .registerModules(KotlinModule(), JavaTimeModule())
        csvMapper.readerFor(StrategyCsvRecord::class.java).with(schema)
    }

    override fun loadStrategy(inputStream: InputStream): List<Strategy>
        = inputStream.use {
            csvReader.readValues<StrategyCsvRecord>(it)
                .readAll()
                .toModels()
        }


}