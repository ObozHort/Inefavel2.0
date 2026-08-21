package com.inefavelstore.bot.services;

import com.inefavelstore.bot.config.Config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RobloxService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String buscarAvatarRoblox(String username) {
        try {
            // 1. Obter ID do Usuario pelo Nickname
            String jsonBody = String.format("{\"usernames\": [\"%s\"], \"excludeBannedUsers\": true}", username);
            HttpRequest requestUser = HttpRequest.newBuilder()
                    .uri(URI.create("https://users.roblox.com/v1/usernames/users"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> responseUser = httpClient.send(requestUser, HttpResponse.BodyHandlers.ofString());
            if (responseUser.statusCode() != 200) return Config.LOGO_LOJA_URL;

            JsonNode rootUser = objectMapper.readTree(responseUser.body());
            JsonNode dataArray = rootUser.get("data");
            if (dataArray == null || !dataArray.isArray() || dataArray.isEmpty()) {
                return Config.LOGO_LOJA_URL;
            }

            long userId = dataArray.get(0).get("id").asLong();

            // 2. Obter URL do Avatar Headshot
            String urlHeadshot = String.format("https://thumbnails.roblox.com/v1/users/avatar-headshot?userIds=%d&size=420x420&format=Png&isCircular=false", userId);
            HttpRequest requestImg = HttpRequest.newBuilder()
                    .uri(URI.create(urlHeadshot))
                    .GET()
                    .build();

            HttpResponse<String> responseImg = httpClient.send(requestImg, HttpResponse.BodyHandlers.ofString());
            if (responseImg.statusCode() != 200) return Config.LOGO_LOJA_URL;

            JsonNode rootImg = objectMapper.readTree(responseImg.body());
            JsonNode imgDataArray = rootImg.get("data");
            if (imgDataArray != null && imgDataArray.isArray() && !imgDataArray.isEmpty()) {
                return imgDataArray.get(0).get("imageUrl").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Config.LOGO_LOJA_URL;
    }
}