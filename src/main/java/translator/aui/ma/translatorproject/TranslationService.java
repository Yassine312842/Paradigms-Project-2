package translator.aui.ma.translatorproject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TranslationService {

    private static final String API_KEY = System.getProperty("OPENROUTER_API_KEY");

    public String translateToDarija(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "No text provided.";
        }

        if (API_KEY == null || API_KEY.isBlank()) {
            return "OpenRouter API key is missing.";
        }

        int maxAttempts = 3;
        int waitMillis = 2000;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String endpoint = "https://openrouter.ai/api/v1/chat/completions";

                URI uri = URI.create(endpoint);
                HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + API_KEY);
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setRequestProperty("HTTP-Referer", "http://localhost");
                connection.setRequestProperty("X-Title", "Darija Translator Project");
                connection.setDoOutput(true);

                String prompt = "Translate the following text into natural Moroccan Arabic Darija. Return only the translation in Darija, with no explanation, no transliteration, and no extra text: " + text;

                String jsonInput = """
                        {
                          "model": "openrouter/free",
                          "messages": [
                            {
                              "role": "user",
                              "content": "%s"
                            }
                          ]
                        }
                        """.formatted(
                        prompt
                                .replace("\\", "\\\\")
                                .replace("\"", "\\\"")
                                .replace("\n", "\\n")
                );

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                    os.write(input);
                }

                int responseCode = connection.getResponseCode();

                if (responseCode == 429 && attempt < maxAttempts) {
                    Thread.sleep(waitMillis);
                    continue;
                }

                Scanner scanner;
                if (responseCode >= 200 && responseCode < 300) {
                    scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
                } else {
                    scanner = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8);
                }

                StringBuilder response = new StringBuilder();
                while (scanner.hasNextLine()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();

                if (responseCode == 429) {
                    return "Translation service is temporarily unavailable due to OpenRouter rate limits. Please try again later.";
                }

                if (responseCode < 200 || responseCode >= 300) {
                    return "Translation service failed. Please try again later.";
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.toString());

                JsonNode textNode = root.path("choices")
                        .path(0)
                        .path("message")
                        .path("content");

                if (textNode.isMissingNode() || textNode.asText().isBlank()) {
                    return "No translation returned by OpenRouter.";
                }

                return textNode.asText().trim();

            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    return "Error while calling OpenRouter API: " + e.getMessage();
                }

                try {
                    Thread.sleep(waitMillis);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    return "Request interrupted.";
                }
            }
        }

        return "Translation failed after multiple attempts.";
    }
}