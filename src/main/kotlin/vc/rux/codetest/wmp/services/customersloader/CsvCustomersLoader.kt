package vc.rux.codetest.wmp.services.customersloader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.services.ICsvCustomersLoader
import java.io.InputStream

@Service
class CsvCustomersLoader : ICsvCustomersLoader {
    private val csvReader by lazy {
        val schema = CsvSchema.emptySchema().withHeader()
        val csvMapper = CsvMapper()
            .configure(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false)
            .registerModules(KotlinModule(), JavaTimeModule())
        csvMapper.readerFor(CustomerCsvRecord::class.java).with(schema)
    }

    override fun loadCustomers(inputStream: InputStream): List<Customer>
        = inputStream.use {
            csvReader.readValues<CustomerCsvRecord>(it)
                .readAll()
                .toModels()
        }


}