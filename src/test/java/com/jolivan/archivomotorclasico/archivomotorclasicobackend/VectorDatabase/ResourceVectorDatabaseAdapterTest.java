package com.jolivan.archivomotorclasico.archivomotorclasicobackend.VectorDatabase;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ResourceVectorDatabaseRepositoryWeaviateAdapter;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers.ResourceVectorDatabaseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResourceVectorDatabaseAdapterTest {
    @Test
    void GetResourceById_OK() {
        ResourceVectorDatabaseRepository db = new ResourceVectorDatabaseRepositoryWeaviateAdapter();
        try {
            db.getResourceById("3e2528f6-584f-49e9-8866-48f479faea8d");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        assert true;
    }

}
