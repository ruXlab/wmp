package vc.rux.codetest.wmp.controllers

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.concurrent.ConcurrentHashMap

/**
 * Note this controller is used for the demonstration purposes only
 * There is a little reason to run app by itself in isolation
 * Instead, tests are convering pretty much most of the functionality
 *
 *
 */
@RestController
@Deprecated("Needs to be replaced with stand alone mock server")
class MockFpsServerForDemoPurposesOnly {
    // dummy storage, keeps things are given
    private val statuses = ConcurrentHashMap<String, Any>().also {
        it["1"] = """{ "customerId": 1, "stocks": 6700, "bonds": 1200, "cash": 400 }"""
        it["2"] = """{ "customerId": 2, "stocks": 1000, "bonds": 2000, "cash": 3000 }"""
    }

    @GetMapping("/customer/{customerId}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCustomer(@PathVariable customerId: String): Any {
        val status = statuses[customerId]
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing found for $customerId");

        log.warn("fetching information for $customerId: $status")
        return status
    }

    @PostMapping("/execute")
    @ResponseStatus(HttpStatus.CREATED)
    fun updateTrades(@RequestBody body: List<Map<String, Any>>) {
        log.warn("updateTrades got new request: {} ", body)
        body.forEach { statuses[(it["customerId"] as Int).toString()] = it }
    }

    companion object {
        private val log = LoggerFactory.getLogger(MockFpsServerForDemoPurposesOnly::class.java)
    }

}