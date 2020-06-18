package vc.rux.codetest.wmp.fpsclient

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import vc.rux.codetest.wmp.models.CustomerId
import vc.rux.codetest.wmp.models.MoneySplit
import vc.rux.codetest.wmp.models.Portfolio

internal class FpsClientTest {
    private lateinit var restTemplate: RestTemplate
    private lateinit var restServiceServer: MockRestServiceServer
    private lateinit var fpsClient: FpsClient

    private val targetFakeBaseUrl = "http://localhost"

    @BeforeEach
    fun init() {
        restTemplate = RestTemplate()

        restServiceServer = MockRestServiceServer.createServer(restTemplate)
        fpsClient = FpsClient(targetFakeBaseUrl, restTemplate)
    }

    @Test
    fun `can retrieve customer details`() {
        // given
        restServiceServer
            .expect { requestTo("$targetFakeBaseUrl/customer/42") }
            .andRespond {
                withStatus(HttpStatus.OK).contentType(APPLICATION_JSON).body("""
                    {
                    "customerId": 123,
                    "stocks": 6700,
                    "bonds": 1200,
                    "cash": 400
                    }
                """.trimIndent()).createResponse(it)
            }


        // when
        val portfolio = fpsClient.getCustomerPortfolio(CustomerId(123))


        // then
        assertThat(portfolio.bonds)
            .isEqualTo(1200)
        assertThat(portfolio.stocks)
            .isEqualTo(6700)
        assertThat(portfolio.cash)
            .isEqualTo(400)
    }


    @Test
    fun `when customer information can not be retrieved the exception is thrown`() {
        // given
        restServiceServer.expect { requestTo(targetFakeBaseUrl) }
            .andRespond {
                withStatus(HttpStatus.BAD_GATEWAY).body("Bad Gateway or any error").createResponse(it)
            }


        // when and then
        assertThrows<RestClientException> {
            fpsClient.getCustomerPortfolio(CustomerId(123))
        }
    }

    @Test
    fun `when trades can not be updated the exception is thrown`() {
        // given
        restServiceServer.expect { requestTo(targetFakeBaseUrl) }
            .andRespond {
                withStatus(HttpStatus.BAD_GATEWAY).body("Bad Gateway or any error").createResponse(it)
            }


        // when and then
        assertThrows<RestClientException> {
            fpsClient.executeTrades(listOf(CustomerId(123) to Portfolio(1, 1, 1)))
        }

    }

}