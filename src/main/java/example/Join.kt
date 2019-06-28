package example

import org.neo4j.procedure.Description
import org.neo4j.procedure.Name
import org.neo4j.procedure.UserFunction

/**
 * This is an example how you can create a simple user-defined function for Neo4j.
 */
class Join {
    @UserFunction
    @Description("example.join(['s1','s2',...], delimiter) - join the given strings with the given delimiter.")
    fun join(
            @Name("strings") strings: List<String>?,
            @Name(value = "delimiter", defaultValue = ",") delimiter: String?): String? {
        return if (strings == null || delimiter == null) {
            null
        } else strings.joinToString(delimiter)
    }
}
