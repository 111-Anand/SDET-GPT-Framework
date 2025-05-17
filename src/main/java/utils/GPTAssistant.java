package utils;

import com.google.gson.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GPTAssistant {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String askGPT(String userPrompt) {
        String apiKey = System.getenv("OPENAI_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            return "OpenAI API key is missing. Please set the OPENAI_API_KEY environment variable.";
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.addHeader("Authorization", "Bearer " + apiKey);
            post.addHeader("Content-Type", "application/json");

            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", userPrompt);

            JsonArray messages = new JsonArray();
            messages.add(message);

            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", "gpt-3.5-turbo");
            requestBody.add("messages", messages);

            StringEntity entity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
            post.setEntity(entity);

            var response = httpClient.execute(post);
            var reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices == null || choices.size() == 0) {
                System.out.println("Unexpected GPT response: " + jsonResponse);
                return "Error: Empty or malformed GPT response.";
            }

            JsonObject messageObject = choices.get(0).getAsJsonObject().getAsJsonObject("message");
            if (messageObject == null || !messageObject.has("content")) {
                System.out.println("Invalid message object: " + jsonResponse);
                return "Error: Missing content in GPT response.";
            }

            return messageObject.get("content").getAsString().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error communicating with GPT.";
        }
    }
}
