package br.com.satheler.bot.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;


public class APIHelper {

    private final URL url;
    private HttpURLConnection connection;

    public APIHelper(String url, String requestMethod) throws MalformedURLException, IOException {
        this.url = new URL(url);
        this.configure(requestMethod);
    }

    private void configure(String requestMethod) throws IOException, RuntimeException {
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection.setRequestMethod(requestMethod);
        this.connection.setRequestProperty("Accept", "application/json");
        this.connection.getContentEncoding();

        if (this.connection.getResponseCode() != 200) {
            throw new RuntimeException("Falha - HTTP Error code: " + this.connection.getResponseCode());
        }
    }

    public String response() throws IOException {
        InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String json = br.readLine();

        return json;
    }

    @Override
    protected void finalize() throws Throwable {
        this.connection.disconnect();
    }
}
