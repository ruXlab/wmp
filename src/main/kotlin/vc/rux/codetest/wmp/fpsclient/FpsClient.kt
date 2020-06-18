package vc.rux.codetest.wmp.fpsclient

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.client.postForEntity
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.Portfolio

class FpsClient(
    private val baseUrl: String,
    private val restTemplate: RestTemplate
) : IFpsClient {

    fun getCustomerPortfolio(customerId: CustomerId): Portfolio {
        val responseEntity = restTemplate.getForEntity<FpsCustomerPortfolioDto>("$baseUrl/customer/{customerId}", customerId.value)
        if (responseEntity.statusCode != HttpStatus.OK)
            throw HttpClientErrorException(responseEntity.statusCode, "Got non 200 response, hasBody: ${responseEntity.body == null}")
        return responseEntity.body?.toModel()
            ?: throw HttpClientErrorException(responseEntity.statusCode, "Has no body in response!")
    }

    fun executeTrades(trades: List<Pair<CustomerId, Portfolio>>) {
        val responseEntity = restTemplate.postForEntity<String>("$baseUrl/execute", trades.toDto())
        if (responseEntity.statusCode == HttpStatus.CREATED)
            throw HttpClientErrorException(responseEntity.statusCode, "Got non 201 response but it's required")
    }
}