package com.jolivan.archivomotorclasicobackend.Resource.VectorDB.Utils;

import com.google.gson.internal.LinkedTreeMap;
import io.weaviate.client.base.Result;
import io.weaviate.client.base.WeaviateError;
import io.weaviate.client.v1.data.model.WeaviateObject;
import io.weaviate.client.v1.graphql.model.GraphQLResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaviateResultConverter {
    private WeaviateResultConverter() {
        throw new IllegalStateException("Utility class");
    }
    public static final String ADDITIONAL = "_additional";
    public static final String HAS_ERRORS = "hasErrors";
    public static final String ERROR_NUMBER = "errorNumber";
    public static final String ERROR_MESSAGES = "errorMessages";

    public static Map<String, Object> ResultGraphQLResponseToMap(Result<GraphQLResponse> DBresult) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();

        if(DBresult.hasErrors()){
            result.put(HAS_ERRORS, true);
            WeaviateError error = DBresult.getError();
            result.put(ERROR_NUMBER, error.getStatusCode());
            result.put(ERROR_MESSAGES, error.getMessages());
        }

        GraphQLResponse response = DBresult.getResult();
        if(response.getErrors() != null){
            result.put(HAS_ERRORS, true);
            result.put(ERROR_MESSAGES, response.getErrors());
        }

        LinkedTreeMap<?,?> data = (LinkedTreeMap<?,?>) response.getData();
        LinkedTreeMap<?,?> Get = (LinkedTreeMap<?,?>) data.get("Get");
        ArrayList<LinkedTreeMap<?,?>> resources = (ArrayList<LinkedTreeMap<?,?>>) Get.get("Resource");
        for (LinkedTreeMap<?,?> resource : resources) {
            Map<String, Object> map = WeaviateResourceToMap(resource);
            resultList.add(map);
        }
        result.put("data", resultList);

        /*
        *Result<GraphQLResponse>
                *|LinkedTreeMap data
                    *|LinkedTreeMap Get
                        *|ArrayList<LinkedTreeMap> Resource
                                    !    |LinkedTreeMap _additional
                                    !       |Double distance
                                    !       |String id
                                    !   |String text
         */

        return result;
    }

    public static Map<String, Object> WeaviateResourceToMap(LinkedTreeMap<?,?> magazine){
        Map<String, Object> result = new HashMap<>();

        magazine.keySet().forEach(key -> {
            if(key.equals(ADDITIONAL)) return;
            result.put(key.toString(), magazine.get(key));
        });

        if(magazine.containsKey(ADDITIONAL)){
            LinkedTreeMap<?,?> _additional = (LinkedTreeMap<?,?>) magazine.get(ADDITIONAL);
            _additional.keySet().forEach(key -> {
                result.put(key.toString(), _additional.get(key));
            });
        }

        /*
            *LinkedTreeMap Resource
                    *|LinkedTreeMap _additional
                       *|Double distance
                       *|String id
                   *|String text
         */

        return result;
    }

    public static Map<String, Object> ResultListWeaviateObjectToMap(Result<List<WeaviateObject>> DBresult){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();

        if(DBresult.hasErrors()){
            result.put(HAS_ERRORS, true);
            WeaviateError error = DBresult.getError();
            result.put(ERROR_NUMBER, error.getStatusCode());
            result.put(ERROR_MESSAGES, error.getMessages());
        } else {
            for(WeaviateObject object : DBresult.getResult()){
                Map<String, Object> map = WeaviateObjectToMap(object);
                resultList.add(map);
            }
            result.put("data", resultList);
        }

        return result;
    }

    public static Map<String, Object> WeaviateObjectToMap(WeaviateObject object) {
        Map<String, Object> result = new HashMap<>();

        object.getProperties().forEach((key, value) -> {
           if(key.equals("properties")){
                LinkedTreeMap<?, ?> properties = (LinkedTreeMap<?, ?>) object.getProperties().get(key);
                properties.keySet().forEach(k -> {
                    result.put(k.toString(), properties.get(k));
                });
           }
           else{
               result.put(key, value);
           }
        });

        return result;
    }

    public static Map<String, Object> ResultListWeaviateObjectToMapSingleResource(Result<List<WeaviateObject>> DBresult) {
        Map<String, Object> result = new HashMap<>();

        if(DBresult.hasErrors()){
            result.put(HAS_ERRORS, true);
            WeaviateError error = DBresult.getError();
            result.put(ERROR_NUMBER, error.getStatusCode());
            result.put(ERROR_MESSAGES, error.getMessages());
        } else {
            WeaviateObject object = DBresult.getResult().get(0);
            result.put("id", object.getId());
            result.put("className", object.getClassName());
            result.put("creationTimeUnix", object.getCreationTimeUnix());
            result.put("lastUpdateTimeUnix", object.getLastUpdateTimeUnix());
            if(object.getProperties() != null) result.putAll(object.getProperties());
            if(object.getAdditional() != null) result.putAll(object.getAdditional());
            if(object.getVector() != null) result.put("vector", object.getVector());
            if(object.getVectorWeights() != null) result.put("vectorWeights", object.getVectorWeights());
        }

        return result;
    }
}
