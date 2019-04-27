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

    /**
     * Construtor da classe com inicialização a partir de uma URL
     */
    public APIHelper(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    /**
     * Método para realizar a configuração da conexão com a URL estabelecida.
     * @param requestMethod Recebe o tipo da requisição (GET, POST, etc.).
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de falha ou interrupção.
     * @throws RuntimeException Constrói uma nova exceção de tempo de execução com nulo como sua mensagem 
     *         de detalhe.
     */
    private void configure(String requestMethod) throws IOException, RuntimeException {
        this.connection = (HttpURLConnection) this.url.openConnection();
        this.connection.setRequestMethod(requestMethod);
        this.connection.setRequestProperty("Accept", "application/json");
    }

    /**
     * Método para realizar uma requisição do tipo GET.
     * @return Resposta da requisição com seu conteúdo.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de falha ou interrupção.
     * @throws RuntimeException Constrói uma nova exceção de tempo de execução com nulo como sua mensagem 
     *         de detalhe.
     */
    public String get() throws IOException, RuntimeException {
        this.configure("GET");

        if (this.connection.getResponseCode() != 200) {
            throw new RuntimeException("Falha - HTTP Error code: " + this.connection.getResponseCode());
        }

        return this.apiResponse();
    }

    /**
     * Método para realizar uma requisição do tipo POST
     * @param values Recebe um MAP para requisitar no método POST e assim ter acesso ao conteúdo.
     * @return Resposta da requisição com seu conteúdo.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de falha ou interrupção.
     * @throws RuntimeException Constrói uma nova exceção de tempo de execução com nulo como sua mensagem 
     *         de detalhe.
     */
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

    /**
     * Método para retornar uma resposta da api requisitada.
     * @return Resposta da requisição.
     * @throws UnsupportedEncodingException A codificação de caracteres não é suportada.
     * @throws IOException Constrói uma nova exceção de tempo de execução com nulo como sua mensagem 
     *         de detalhe.
     */
    private String apiResponse() throws UnsupportedEncodingException, IOException {
        InputStreamReader in = new InputStreamReader(connection.getInputStream(), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        String jsonResponse = br.readLine();

        return jsonResponse;
    }

    /**
     * Método para fazer a conversão do MAP para Json.
     * @param values Recebe MAP com os valores para conversão.
     * @return String com Map convertido para Json.
     * @throws JsonProcessingException Classe base intermediária para todos os problemas encontrados 
     *         durante o processamento (análise, geração) de conteúdo JSON que não são problemas 
     *         puros de I/O.
     */
    private String mapToJson(Map<String, Object> values) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(values);
    }

    /**
     * Método para finalizar a conexão com a url requisitada.
     */
    @Override
    protected void finalize() throws Throwable {
        this.connection.disconnect();
    }
}
