package vc.rux.codetest.wmp

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import vc.rux.codetest.wmp.services.RebalancingService

@SpringBootApplication
class WmpApplication(
    private val rebalancingService: RebalancingService
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        rebalancingService.rebalanceCustomers()
    }
}

fun main(args: Array<String>) {
    runApplication<WmpApplication>(*args)
}
