
Wealth management platform _(code test)_
-----------------------


### Implementation notes

* The age of Bob given in example is 59 not 51 as it was in task
* `StrategySelector` when multiple strategies available chooses the one with minimal risk 


### Tradeoff / room for improvements

* Each component(csv parser, evaluator, fps client) could be implemented as independent module to reduce coupling
* Number of additional validations could be implemented. For instance sum of wealth distributions must equal to 100%
* Instead of loading everything in memory streaming could be used instead
* If there are a considerable amount of strategies and parameters it worth to ditribute them in indexed buckets to increase lookup speed