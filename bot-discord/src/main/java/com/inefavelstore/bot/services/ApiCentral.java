package com.inefavelstore.bot.services;

import okhttp3.*;
import java.io.IOException;

public class ApiCentral {

    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://localhost:3000"; // Altere para a URL da sua API

    public String buscarInformacoes(String endpoint) {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                return response.body().string();
            } else {
                Logger.warn("Erro na resposta da API Central: " + response.code());
            }
        } catch (IOException e) {
            Logger.error("Falha ao comunicar com a API Central no endpoint: " + endpoint, e);
        }
        return null;
    }

    public boolean enviarDados(String endpoint, String jsonBody) {
        RequestBody body = RequestBody.create(jsonBody, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            Logger.error("Falha ao enviar dados para a API Central", e);
            return false;
        }
    }
}