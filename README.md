# Neo4j Proximity Score Plugin
This is a user-defined function for Neo4j which returns a proximity score for two nodes. The proximity score is defined as *proximity(A, B) = log(1 + paths(A, B))*, where *A* and *B* are two nodes in the graph and *paths(A, B)* is the number of all shortest paths between *A* and *B*.
## Test & Build
```shell
gradle build
```
## Deploy
Copy the built jar file found in `./build/libs/` to the Neo4j `plugins` directory. For the default Neo4j `plugins` path on Ubuntu 16.04+:
```shell
sudo cp ./build/libs/neo4j-proximity-score-plugin.jar /var/lib/neo4j/plugins/
```
After copying the jar file, you need to restart the Neo4j service. On Ubuntu 16.04+:
```shell
sudo service neo4j restart
```
## Usage
Now you can use the function `com.github.soroushj.proximityScore` in Cypher queries:
```cypher
MATCH (a:User {username: "alice"}), (b:User {username: "bob"})
RETURN com.github.soroushj.proximityScore(a, b)
```
