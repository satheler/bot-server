package br.com.satheler.bot.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class APIHelper {

    private final URL url;
    private HttpURLConnection connection;

    public APIHelper(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private void configure(String requestMethod) throws IOException, RuntimeException {
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection.setRequestMethod(requestMethod);
        this.connection.setRequestProperty("Accept", "application/json");
    }

    public String get() throws IOException, RuntimeException {
        this.configure("GET");

        if (this.connection.getResponseCode() != 200) {
            throw new RuntimeException("Falha - HTTP Error code: " + this.connection.getResponseCode());
        }

        return this.apiResponse();
    }

    public String post(Map<String, Object> values) throws IOException, RuntimeException {
        this.configure("POST");
        this.connection.setDoOutput(true);
        PrintStream printStream = new PrintStream(connection.getOutputStream());
        String json = this.mapToJson(values);
        printStream.println(json);
        connection.connect();

        if (this.connection.getResponseCode() != 200) {
            throw new RuntimeException(":() ");
        }

        return this.apiResponse();
    }

    private String apiResponse() throws UnsupportedEncodingException, IOException {
        InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String jsonResponse = br.readLine();

        return jsonResponse;
    }

    private String mapToJson(Map<String, Object> values) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(values);
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.disconnect();
    }
}
