package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GPTAssistant {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String ENDPOINT = "https://api.openai.com/v1/chat/completions";

    public static String askGPT(String prompt) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            return "Error: OpenAI API key is missing. Set it using the OPENAI_API_KEY environment variable.";
        }

        try {
            URL url = new URL(ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);

            String body = "{\n" +
                    "  \"model\": \"gpt-3.5-turbo\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"user\", \"content\": \"" + prompt + "\"}\n" +
                    "  ]\n" +
                    "}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int status = conn.getResponseCode();
            InputStream is = (status < 400) ? conn.getInputStream() : conn.getErrorStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            conn.disconnect();

            String result = response.toString();
            if (result.contains("choices")) {
                int start = result.indexOf("\"content\":\"") + 11;
                int end = result.indexOf("\"", start);
                return result.substring(start, end).replace("\\n", "\n");
            } else {
                return "Error: Empty or malformed GPT response.";
            }

        } catch (Exception e) {
            return "Exception while calling OpenAI: " + e.getMessage();
        }
    }
}
