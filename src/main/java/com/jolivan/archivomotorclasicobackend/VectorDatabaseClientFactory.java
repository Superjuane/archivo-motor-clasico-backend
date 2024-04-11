package com.jolivan.archivomotorclasicobackend;

import io.weaviate.client.Config;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.v1.misc.model.Meta;

public class VectorDatabaseClientFactory {

    private static VectorDatabaseClientFactory instance;

    private Config config;
    private WeaviateClient client;
    private Result<Meta> meta;

    // Private constructor to prevent instantiation outside the class
    private VectorDatabaseClientFactory() {

        config = new Config("http", "localhost:8080");
        client = new WeaviateClient(config);
        meta = client.misc().metaGetter().run();
        if (meta.getError() == null) {
            System.out.printf("meta.hostname: %s\n", meta.getResult().getHostname());
            System.out.printf("meta.version: %s\n", meta.getResult().getVersion());
            System.out.printf("meta.modules: %s\n", meta.getResult().getModules());
        } else {
            System.out.printf("Error: %s\n", meta.getError().getMessages());
        }
    }

    // Public method to get the instance of the singleton class
    public static VectorDatabaseClientFactory getInstance() {
        if (instance == null) {
            synchronized (VectorDatabaseClientFactory.class) {
                if (instance == null) {
                    instance = new VectorDatabaseClientFactory();
                }
            }
        }
        System.out.println(instance);
        return instance;
    }

    // Getter method for the client variable
    public WeaviateClient getClient() {
        return client;
    }

    public Config getConfig() {
        return config;
    }

    // You can add other methods or operations related to the client as needed
}
