package com.github.soroushj;

import org.junit.Test;
import org.junit.Rule;
import org.junit.Assert;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Session;

public class ProximityScoreTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule().withFunction(ProximityScore.class);

    @Test
    public void testProximityScore() {
        try (Driver driver = GraphDatabase.driver(neo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {
            Session session = driver.session();
            session.run(
                    "CREATE " +
                    "(x:X), " +
                    "(y:Y), " +
                    "(a), " +
                    "(b), " +
                    "(c), " +
                    "(d), " +
                    "(x)-[:R]->(a)-[:R]->(y), " +
                    "(x)-[:R]->(b)-[:R]->(y), " +
                    "(x)-[:R]->(c)-[:R]->(d)-[:R]->(y)");
            double proximity = session.run(
                    "MATCH (x:X), (y:Y) " +
                    "RETURN com.github.soroushj.proximityScore(x, y) AS prox")
                    .single().get("prox").asDouble();
            Assert.assertEquals(1.0986122886681098, proximity, 1e-16);
        }
    }

    @Test
    public void testZeroProximityScore() {
        try (Driver driver = GraphDatabase.driver(neo4j.boltURI(), Config.build().withoutEncryption().toConfig())) {
            Session session = driver.session();
            session.run(
                    "CREATE " +
                    "(x:X), " +
                    "(y:Y)");
            double proximity = session.run(
                    "MATCH (x:X), (y:Y) " +
                    "RETURN com.github.soroushj.proximityScore(x, y) AS prox")
                    .single().get("prox").asDouble();
            Assert.assertEquals(0, proximity, 1e-16);
        }
    }
}
