package br.com.satheler.bot.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.satheler.bot.providers.ServiceProvider;

/**
 * Application main
 */
public final class App {

    public static ServerSocket SERVER_SOCKET;
    public static ServiceProvider SERVICE_PROVIDER;

    /**
     * Método para inicializar todo o servidor e a aplicação JAVA.
     * @param args[] Passagem de argumentos caso necessário à aplicação.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo
     *         de falha ou interrupção.
     */
    public static void main(String args[]) throws IOException {
        int port = 1234;

        try {
            SERVICE_PROVIDER = ServiceProvider.getInstance();
            SERVER_SOCKET = new ServerSocket(port);
            System.out.println("SERVIDOR RODANDO NA PORTA " + port);
            while (true) {
                Socket conexao = SERVER_SOCKET.accept();
                Thread newThread = new Server(conexao);
                newThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SERVER_SOCKET.close();
        }
    }
}
