package br.com.satheler.bot.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Map;

import br.com.satheler.bot.providers.ServiceProvider;

/**
 * Server
 */
public class Server extends Thread {

    private Socket connSocket;
    private String ipAddress;

    public Server(Socket socket) {
        this.connSocket = socket;
        this.ipAddress = socket.getRemoteSocketAddress().toString().replace("/", "");
        this.log("Conectou ao servidor!");
    }

    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(this.connSocket.getInputStream()));
            PrintStream client = new PrintStream(this.connSocket.getOutputStream());

            String request = input.readLine();
            String response = null;

            while (request != null) {
                this.log("Requisitou o comando: " + request);
                try {
                    Map<String, Object> inputFormatted = ServiceProvider.prepareRequest(request);
                    if (ServiceProvider.commandIsAsynchronous()) {
                        // client.println("Aguarde...");
                        this.log("Usuário aguardando dados.");
                    }
                    response = ServiceProvider.runRequest(inputFormatted);
                } catch (InputMismatchException | NullPointerException e) {
                    response = e.getMessage();
                } finally {
                    client.println(response);
                    this.log("Resposta enviada para o cliente.");
                    request = input.readLine();
                }
            }

            this.connSocket.close();
        } catch (IOException e) {
        } finally {
            this.log("Desconectado.");
        }
    }

    private void log(String log) {
        DateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy HH:mm:ss] - " + ipAddress + ": ");
        String now = dateFormat.format(new Date());

        System.out.println(now + log);
    }
}
