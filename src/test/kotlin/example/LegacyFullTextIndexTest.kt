package example

import org.junit.Rule
import org.junit.Test
import org.neo4j.driver.v1.*
import org.neo4j.graphdb.factory.GraphDatabaseSettings
import org.neo4j.harness.junit.Neo4jRule

import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.neo4j.driver.v1.Values.parameters

class LegacyFullTextIndexTest {
    // This rule starts a Neo4j instance for us
    @Rule
    @JvmField
    val neo4j: Neo4jRule = Neo4jRule()

            // This is the Procedure we want to test
            .withProcedure(FullTextIndex::class.java)

    @Test
    @Throws(Throwable::class)
    fun shouldAllowIndexingAndFindingANode() {
        // In a try-block, to make sure we close the driver and session after the test
        GraphDatabase.driver(neo4j.boltURI(), Config.build()
                .withEncryptionLevel(Config.EncryptionLevel.NONE).toConfig()).use { driver ->
            driver.session().use { session ->
                // Given I've started Neo4j with the FullTextIndex procedure class
                //       which my 'neo4j' rule above does.
                // And given I have a node in the database
                val nodeId = session.run("CREATE (p:User {name:'Brookreson'}) RETURN id(p)")
                        .single()
                        .get(0).asLong()

                // When I use the index procedure to index a node
                session.run("CALL example.index({id}, ['name'])", parameters("id", nodeId))

                // Then I can search for that node with lucene query syntax
                val result = session.run("CALL example.search('User', 'name:Brook*')")
                assertThat(result.single().get("nodeId").asLong(), equalTo<Long>(nodeId))
            }
        }
    }
}
