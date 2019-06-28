package example

import org.neo4j.procedure.*

/**
 * This is an example how you can create a simple user-defined function for Neo4j.
 */
class Last {
    @UserAggregationFunction("example.last")
    @Description("example.last(value) - returns last non-null row")
    fun last(): LastFunction {
        return LastFunction()
    }

    class LastFunction {

        private var lastValue: Any? = null

        @UserAggregationUpdate
        fun aggregate(@Name("value") value: Any?) {
            if (value != null) {
                this.lastValue = value
            }
        }

        @UserAggregationResult
        fun result(): Any? {
            return lastValue
        }
    }
}
