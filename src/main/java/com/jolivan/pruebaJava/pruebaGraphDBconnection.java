package com.jolivan.pruebaJava;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.QueryConfig;
import org.neo4j.driver.Record;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.TransactionContext;
import org.neo4j.driver.exceptions.NoSuchRecordException;

public class pruebaGraphDBconnection {

    // Create & employ 100 people to 10 different organizations
    public static void main(String... args) {

        final String dbUri = "bolt://localhost:7687";
        final String dbUser = "jake";
        final String dbPassword = "abcd5678";



        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            try (var session = driver.session(SessionConfig.builder().withDatabase("neo4j").build())) {
                var result = driver.executableQuery("MATCH (n:Person) RETURN n.name AS name, n.age AS age LIMIT 25")
                        .withConfig(QueryConfig.builder().withDatabase("neo4j").build())
                        .execute();
                var records = result.records();
                records.forEach(r -> {
                    System.out.println(r);
                    System.out.println(r.get("name").asString());
                    System.out.println(r.get("age").asString());
                });

// Summary information
                var summary = result.summary();
                System.out.printf("The query %s returned %d records in %d ms.%n",
                        summary.query(), records.size(),
                        summary.resultAvailableAfter(TimeUnit.MILLISECONDS));

                session.close();
                driver.close();
//                for (int i=0; i<100; i++) {
//                    String name = String.format("Thor%d", i);
//
//                    try {
//                        String orgId = session.executeWrite(tx -> employPersonTx(tx, name));
//                        System.out.printf("User %s added to organization %s.%n", name, orgId);
//                    } catch (Exception e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
            }
        }
        // import java.util.Map;
// import org.neo4j.driver.QueryConfig;

// Get all 42-year-olds
//        var result = driver.executableQuery("MATCH (p:Person {age: $age}) RETURN p.name AS name")
//                .withParameters(Map.of("age", 42))
//                .withConfig(QueryConfig.builder().withDatabase("neo4j").build())
//                .execute();

// Loop through results and do something with them
//        var records = result.records();
//        records.forEach(r -> {
//            System.out.println(r);  // or r.get("name").asString()
//        });
//
//// Summary information
//        var summary = result.summary();
//        System.out.printf("The query %s returned %d records in %d ms.%n",
//                summary.query(), records.size(),
//                summary.resultAvailableAfter(TimeUnit.MILLISECONDS));
    }

    static String employPersonTx(TransactionContext tx, String name) {
        final int employeeThreshold = 10;

        // Create new Person node with given name, if not exists already
        tx.run("MERGE (p:Person {name: $name})", Map.of("name", name));

        // Obtain most recent organization ID and the number of people linked to it
        var result = tx.run("""
            MATCH (o:Organization)
            RETURN o.id AS id, COUNT{(p:Person)-[r:WORKS_FOR]->(o)} AS employeesN
            ORDER BY o.createdDate DESC
            LIMIT 1
            """);

        Record org = null;
        String orgId = null;
        int employeesN = 0;
        try {
            org = result.single();
            orgId = org.get("id").asString();
            employeesN = org.get("employeesN").asInt();
        } catch (NoSuchRecordException e) {
            // The query is guaranteed to return <= 1 results, so if.single() throws, it means there's none.
            // If no organization exists, create one and add Person to it
            orgId = createOrganization(tx);
            System.out.printf("No orgs available, created %s.%n", orgId);
        }

        // If org does not have too many employees, add this Person to it
        if (employeesN < employeeThreshold) {
            addPersonToOrganization(tx, name, orgId);
            // If the above throws, the transaction will roll back
            // -> not even Person is created!

            // Otherwise, create a new Organization and link Person to it
        } else {
            orgId = createOrganization(tx);
            System.out.printf("Latest org is full, created %s.%n", orgId);
            addPersonToOrganization(tx, name, orgId);
            // If any of the above throws, the transaction will roll back
            // -> not even Person is created!
        }

        return orgId;  // Organization ID to which the new Person ends up in
    }

    static String createOrganization(TransactionContext tx) {
        var result = tx.run("""
            CREATE (o:Organization {id: randomuuid(), createdDate: datetime()})
            RETURN o.id AS id
        """);
        var org = result.single();
        var orgId = org.get("id").asString();
        return orgId;
    }

    static void addPersonToOrganization(TransactionContext tx, String personName, String orgId) {
        tx.run("""
            MATCH (o:Organization {id: $orgId})
            MATCH (p:Person {name: $name})
            MERGE (p)-[:WORKS_FOR]->(o)
            """, Map.of("orgId", orgId, "name", personName)
        );
    }
}