package com.github.soroushj;

import java.util.Map;
import java.util.HashMap;
import java.lang.RuntimeException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.procedure.UserFunction;
import org.neo4j.procedure.Name;

public class ProximityScore {
    @UserFunction
    @Description("Returns the proximity score of two nodes.")
    public double proximityScore(@Name("Node A") Node nodeA, @Name("Node B") Node nodeB) {
        if (nodeA == null || nodeB == null) {
            throw new RuntimeException("Nodes must not be null.");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("idA", nodeA.getId());
        params.put("idB", nodeB.getId());
        try (Result result = nodeA.getGraphDatabase().execute(
                "MATCH p = allShortestPaths((a)-[*]-(b)) " +
                "WHERE id(a) = $idA AND id(b) = $idB " +
                "RETURN log(1 + count(p)) AS prox", params)) {
            return (double)result.next().get("prox");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
