package com.example.minihub.domain;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;


public class Payload {
    public String action;

    public String ref_tag;

    public String ref;

    public Repository repo;

    public int size;

    public boolean merged;

    public static class PayloadDeserializer implements JsonDeserializer<Payload> {

        @Override
        public Payload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Payload payload = new Gson().fromJson(json, Payload.class);
            JsonObject jsonObject = json.getAsJsonObject();
            if (jsonObject.has("action")) {
                JsonElement element = jsonObject.get("action");

                if (element != null && !element.isJsonNull()) {
                    payload.action = element.getAsString();
                }
            }

            if (jsonObject.has("ref_tag")) {
                JsonElement element = jsonObject.get("ref_tag");

                if (element != null && !element.isJsonNull()) {
                    payload.ref_tag = element.getAsString();
                }
            }

            if (jsonObject.has("ref")) {
                JsonElement element = jsonObject.get("ref");
                if (element != null && !element.isJsonNull()) {
                    payload.ref = element.getAsString();
                }
            }
            if (jsonObject.has("pull_request")) {
                JsonElement element = jsonObject.get("pull_request");
                JsonObject pullRequestObject = element.getAsJsonObject();
                if (pullRequestObject.has("merged")) {
                    JsonElement mergedElement = pullRequestObject.get("merged");
                    if (mergedElement != null && !mergedElement.isJsonNull()) {
                        payload.merged = mergedElement.getAsBoolean();
                    }
                }
            }


            if (jsonObject.has("size")) {
                JsonElement element = jsonObject.get("size");

                if (element != null && !element.isJsonNull()) {
                    payload.size = element.getAsInt();
                }
            }


            return payload;
        }

    }
}
