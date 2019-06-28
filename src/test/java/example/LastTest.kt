package example

import org.junit.Rule
import org.junit.Test

import org.neo4j.driver.v1.Config
import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.GraphDatabase
import org.neo4j.driver.v1.Session
import org.neo4j.harness.junit.Neo4jRule

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.*

class LastTest {
    // This rule starts a Neo4j instance
    @Rule
    @JvmField
    val neo4j: Neo4jRule = Neo4jRule()

            // This is the function we want to test
            .withAggregationFunction(Last::class.java)

    @Test
    @Throws(Throwable::class)
    fun shouldAllowReturningTheLastValue() {
        // This is in a try-block, to make sure we close the driver after the test
        GraphDatabase
                .driver(neo4j.boltURI(), Config.build().withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig()).use { driver ->
                    // Given
                    val session = driver.session()

                    // When
                    val result = session.run("UNWIND range(1,10) as value RETURN example.last(value) AS last").single().get("last").asLong()

                    // Then
                    assertThat(result, equalTo(10L))
                }
    }
}
