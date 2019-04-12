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

    public APIHelper(String url) throws MalformedURLException, IOException {
        this.url = new URL(url);
        this.configure();
    }

    private void configure() throws IOException, RuntimeException {
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection.setRequestMethod("GET");
        this.connection.setRequestProperty("Accept", "application/json");

        if (this.connection.getResponseCode() != 200) {
            throw new RuntimeException("Falha - HTTP Error code: " + this.connection.getResponseCode());
        }
    }

    public String getData() throws IOException {
        InputStreamReader in = new InputStreamReader(connection.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        
        return output;
    } 

    @Override
    protected void finalize() throws Throwable {
        this.connection.disconnect();
    }
}