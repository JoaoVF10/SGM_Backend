package br.edu.ifpb.sgm.projeto_sgm.service;

import java.net.URI;
import java.net.http.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class SuapService {

    private static final String LOGIN_URL = "https://suap.ifpb.edu.br/api/jwt/obtain_token/";
    private static final String ALUNOS_URL = "https://suap.ifpb.edu.br/api/ensino/alunos/v1/";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String obterToken(String username, String password) throws Exception {
        JSONObject credentials = new JSONObject();
        credentials.put("username", username);
        credentials.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(credentials.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject responseBody = new JSONObject(response.body());
            return responseBody.getString("access");
        } else {
            System.out.println("Erro ao obter token: " + response.body());
            return null;
        }
    }

    public String consultarDadosAluno(String token) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ALUNOS_URL))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONArray alunos = new JSONArray(response.body());
            if (!alunos.isEmpty()) {
                // Retorna apenas o primeiro aluno, você pode adaptar
                return alunos.getJSONObject(0).toString();
            } else {
                return "{}"; // Nenhum aluno encontrado
            }
        } else {
            System.out.println("Erro ao consultar aluno: " + response.body());
            return null;
        }
    }
}
