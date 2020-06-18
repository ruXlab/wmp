
Wealth management platform _(code test)_
===========================

## What is it?

It's a code test - wealth management platform sketch.

> A customer's portfolio is made up of varying amounts of different assets; stocks, bonds and cash.
> The current state of the financial market, e.g. are stocks going up or down in price. A daily rebalancing
process occurs to make sure each customer's portfolio matches:
>> The customer's risk level, e.g. does someone want to risk losing some money in order to potentially make a lot, or take fewer risks in exchange for lower gains
>
>> The customer's financial goals, e.g. they are saving to buy a house in 5 years, or they are saving for retirement in 25 years
>
>> Your task is to build a service to automate this daily rebalancing.
>
> Your daily rebalancing service is provided 2 CSV files every day to process customers.csv and strategy.csv .

## How to run it?

You'll only need unix-like computer with bash and java11+ installed

**RUN TESTS**

`./mvmw test`

Tests are written for every component and service testing most important functionality. 
Take look at [RebalancingServiceEndToEndTest](src/test/kotlin/vc/rux/codetest/wmp/services/RebalancingServiceEndToEndTest.kt)
which tests core functionality end to end 

**RUN APP**

`./mvnw spring-boot:run`

Note that it starts a mock web server which simulates FPS (third party). That could be easily configured via configuration file.
Service will start, perform calculation and send request to the FPS service with instructing to update some assets
in a portfolio of each customer. Dummy service actually displays that request in the logs.


### Implementation notes

* The age of Bob given in example is 59 not 51 as it was in task.
* When multiple strategies available `StrategySelector` chooses the one with minimal risk 
* For convenience and not expressed preferences the sample csv provided are located in the resources folder but it could be easily configured in the bean factory
* I assumed that portfolio has assigned money value expressed through some units which are distributed in by different assets categories.
* If there is no portfolio stored on the FPS this customer will be skipped

### Tradeoff / room for improvements

* Each component(csv parser, evaluator, fps client) could be implemented as independent module to reduce coupling
* Number of additional validations could be implemented. For instance sum of wealth distributions must equal to 100%
* Instead of loading everything in memory streaming could be used instead
* If there are a considerable amount of strategies and parameters it worth to distribute them in indexed buckets to increase lookup speed
* `FPSService` can execute requests in parallel allowing parallel computation
* `BigDecimal` has be used everywhere instead of Double
* If operator overloading is acceptable in the team `MoneySplit` could be multiplied by `Portfolio` for readability
* And many more


-----------

by [Ruslan Zaharov](https://rux.vc)

  