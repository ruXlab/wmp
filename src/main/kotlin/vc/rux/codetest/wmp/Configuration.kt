package vc.rux.codetest.wmp

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.web.client.RestTemplate
import vc.rux.codetest.wmp.fpsclient.FpsClient
import vc.rux.codetest.wmp.fpsclient.IFpsClient
import vc.rux.codetest.wmp.models.Customer
import vc.rux.codetest.wmp.models.Strategy
import vc.rux.codetest.wmp.services.customersloader.CsvCustomersLoader
import vc.rux.codetest.wmp.services.strategiesloader.CsvStrategyLoader

@Configuration
class Configuration {
//    @Bean
//    fun provideStrategyLoader(
//    ): ICsvStrategyLoader {
//
//    }

    @Bean
    fun provideRestTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun provideFpsClient(
        @Value("\${fps.baseUrl}") fpsBaseUrl: String,
        restTemplate: RestTemplate
    ): IFpsClient
        = FpsClient(fpsBaseUrl, restTemplate)

    @Bean
    fun provideCustomers(
        @Value("classpath:data/customers.csv") resource: Resource,
        csvCustomersLoader: CsvCustomersLoader
    ): List<Customer>
        = csvCustomersLoader.loadCustomers(resource.inputStream)

    @Bean
    fun provideStrategies(
        @Value("classpath:data/strategies.csv") resource: Resource,
        csvStrategyLoader: CsvStrategyLoader
    ): List<Strategy>
        = csvStrategyLoader.loadStrategy(resource.inputStream)

}