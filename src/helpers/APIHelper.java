package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        String output = br.readLine();
        
        return output;
    } 

    @Override
    protected void finalize() throws Throwable {
        this.connection.disconnect();
    }
}