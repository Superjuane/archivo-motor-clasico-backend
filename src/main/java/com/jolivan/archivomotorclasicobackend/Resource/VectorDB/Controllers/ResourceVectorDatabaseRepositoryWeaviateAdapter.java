package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Controllers;

import com.google.gson.internal.LinkedTreeMap;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.ExceptionControl.Exceptions.IdIsNullException;
import com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils.WeaviateResultConverter;
import com.jolivan.archivomotorclasicobackend.VectorDatabaseClientFactory;
import io.weaviate.client.WeaviateClient;
import io.weaviate.client.base.Result;
import io.weaviate.client.base.WeaviateErrorMessage;
import io.weaviate.client.v1.data.model.WeaviateObject;
import com.jolivan.archivomotorclasicobackend.Utils.Pair;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;
import io.weaviate.client.v1.graphql.query.argument.NearImageArgument;
import io.weaviate.client.v1.graphql.query.fields.Field;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ResourceVectorDatabaseRepositoryWeaviateAdapter implements ResourceVectorDatabaseRepository {

    public static final WeaviateClient client = VectorDatabaseClientFactory.getInstance().getClient();

    @Override
    public List<Map<String, Object>> getAllResources() {
        return getAllResources(0, 20);
    }

    @Override
    public List<Map<String, Object>> getAllResources(int page, int size) {

        List<Map<String, Object>> result = new ArrayList<>();

        Field title = Field.builder()
                .name("image title description")
                .build();
        Field _additional = Field.builder()
                .name("_additional")
                .fields(new Field[]{
                        Field.builder().name("id").build()
                }).build();

        Result<GraphQLResponse> DBresult = client.graphQL()
                .get()
                .withClassName("Resource")
                .withFields(title, _additional)
                .run();

        //TODO: make throw
        //
        // error and catch it in the repository to return repo.blank()
        if (DBresult.hasErrors()) {
            System.out.println(DBresult.getError());
            return null;
        }

//        TODO: WeaviateResultConverter.resultToResourceList(DBresult.getResult().getData());
        result = (List<Map<String, Object>>)WeaviateResultConverter.ResultGraphQLResponseToMap(DBresult).get("data");

//        Object r = DBresult.getResult().getData();
//        LinkedTreeMap<?,?> rl = (LinkedTreeMap<?,?>) r;
//        rl = (LinkedTreeMap<?,?>) rl.get("Get");
//        ArrayList<?> resultList = (ArrayList<?>) rl.get("Resource");
//        for (Object o : resultList) {
//
//            Map<String, Object> resource = new HashMap<>();
//
//            rl = (LinkedTreeMap<?, ?>) o;
//            String image = (String) rl.get("image");
//            String text = (String) rl.get("text");
//
//            rl = (LinkedTreeMap<?, ?>) rl.get("_additional");
//            String id = (String) rl.get("id");
//
//            resource.put("id", id);
//            resource.put("image", image);
//            resource.put("text", text);
//
//            result.add(resource);
//        }


        return result;
    }

    @Override
    public List<String> getAllResourcesIds() {
        List<String> result = new ArrayList<>();

        Field _additional = Field.builder()
                .name("_additional")
                .fields(new Field[]{
                        Field.builder().name("id").build()
//                        Field.builder().name("distance").build()
                }).build();

        Result<GraphQLResponse> queryResult = client.graphQL()
                .get()
                .withClassName("Resource")
                .withFields(_additional)
//                .withNearText(explore)
                .run();

        Map<String, Object> DBresultMap = WeaviateResultConverter.ResultGraphQLResponseToMap(queryResult);
        for (Map<String, Object> r : (List<Map<String, Object>>) DBresultMap.get("data")) {
            result.add((String) r.get("id"));
        }

        return result;
    }

    @Override
    public Map<String, Object> getResourceById(String id) throws IdIsNullException, Throwable {
        if(id == null) throw new IdIsNullException("No ID for resource request in ResourceVectorDatabaseRepositoryWeaviateAdapter.getResourceById");

    Map<String, Object> resultMap = new HashMap<>();

    Result<List<WeaviateObject>> DBresult = client.data().objectsGetter()
            .withClassName("Resource")
            .withID(id)
            .run();

    if(DBresult.hasErrors()){
        for(WeaviateErrorMessage error : DBresult.getError().getMessages()){
            throw error.getThrowable();
        }
    }

    if (DBresult.hasErrors()) {
        resultMap.put("hasError", true);
        resultMap.put("errorNumber", DBresult.getError().getStatusCode());
        List<WeaviateErrorMessage> DBerrorMessages = DBresult.getError().getMessages();
        List <Pair<String, Throwable>> resultErrorMessages = new ArrayList<>();
        for (WeaviateErrorMessage e : DBerrorMessages) {
            Pair<String, Throwable> error = new Pair<>(e.getMessage(), e.getThrowable());
            resultErrorMessages.add(error);
        }
        resultMap.put("errorMessages", resultErrorMessages);
    }
    else {
        resultMap.put("hasError", false);
        for (WeaviateObject object : DBresult.getResult()) {
            resultMap.put("id", object.getId());
            resultMap.put("className", object.getClassName());
            resultMap.put("creationTimeUnix", object.getCreationTimeUnix());
            resultMap.put("lastUpdateTimeUnix", object.getLastUpdateTimeUnix());

            LinkedTreeMap<?,?> properties = (LinkedTreeMap<?,?>) object.getProperties();//class: 'com.google.gson.internal.LinkedTreeMap'
            Map<String, Object> auxMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                auxMap.put(entry.getKey().toString(), entry.getValue());
            }
            resultMap.put("properties", auxMap);

            resultMap.put("additional", object.getAdditional());
            resultMap.put("vector", object.getVector());
            resultMap.put("vectorWeights", object.getVectorWeights());
        }
    }

    return WeaviateResultConverter.ResultListWeaviateObjectToMapSingleResource(DBresult);
}

    @Override
    public List<Map<String, Object>> getResourcesByImageSimilarity(String image, Integer limit) throws Throwable {
        List<Map<String, Object>> result = new ArrayList<>();

        NearImageArgument.NearImageArgumentBuilder nearImageArgumentBuilder = NearImageArgument.builder().image(image);

        Field title = Field.builder()
                .name("title description")
                .build();
        Field _additional = Field.builder()
                .name("_additional")
                .fields(new Field[]{
                        Field.builder().name("id").build(),
                        Field.builder().name("distance").build()
                }).build();

        Result<GraphQLResponse> DBresultRaw = client.graphQL().get()
                .withClassName("Resource")
                .withNearImage(nearImageArgumentBuilder.build())
                .withFields(title, _additional)
                .withLimit(limit)
                .run();

         Map<String, Object> DBresultMap = WeaviateResultConverter.ResultGraphQLResponseToMap(DBresultRaw);
         if(DBresultMap.containsKey("hasErrors") && (Boolean) DBresultMap.get("hasErrors")){
             throw new Throwable("Error --> ResourceVectorDatabaseRepositoryWeaviateAdapter.getResourcesByImageSimilarity: Error...");
         }

         return (List<Map<String, Object>>) DBresultMap.get("data");
    }

    @Override
    public Map<String, Object> insertResource(Map<String, Object> data) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("title", data.get("title"));
        properties.put("description", data.get("description"));
        if(data.get("image").toString().startsWith("data:image/jpeg;base64,")){
            data.put("image", data.get("image").toString().replace("data:image/jpeg;base64,", ""));
        }
        properties.put("image", data.get("image"));


        Result<WeaviateObject> DBresult = client.data()
                .creator()
                .withClassName("Resource")
                .withProperties(properties)
                .run();

        Map<String, Object> resultMap = new HashMap<>();

        WeaviateObject object = DBresult.getResult();
            resultMap.put("id", object.getId());
            resultMap.put("className", object.getClassName());
            resultMap.put("creationTimeUnix", object.getCreationTimeUnix());
            resultMap.put("lastUpdateTimeUnix", object.getLastUpdateTimeUnix());

            LinkedTreeMap<?,?> props = (LinkedTreeMap<?,?>) object.getProperties();//class: 'com.google.gson.internal.LinkedTreeMap'
            Map<String, Object> auxMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : props.entrySet()) {
                auxMap.put(entry.getKey().toString(), entry.getValue());
            }
            resultMap.put("properties", auxMap);

            resultMap.put("additional", object.getAdditional());
            resultMap.put("vector", object.getVector());
            resultMap.put("vectorWeights", object.getVectorWeights());

        Map<String, Object> DBresultMap = WeaviateResultConverter.WeaviateObjectToMap(DBresult.getResult());

        return resultMap;
    }

    @Override
    public Map<String, Object> updateResource(String id, Map<String, Object> data) {
        return null;
    }

    @Override
    public Map<String, Object> deleteResource(String id) {
        return null;
    }
}
