package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

import models.ICommand;

/**
 * Server
 */
public class Server extends Thread {

    private Socket connSocket;

    public Server(Socket socket) {
        this.connSocket = socket;
    }

    public void run() {
        String ipAddress = "undefined";
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.connSocket.getInputStream()));
            PrintStream client = new PrintStream(this.connSocket.getOutputStream());
            ipAddress = this.connSocket.getRemoteSocketAddress().toString();

            System.out.println(ipAddress + ": conectado ao servidor!");
            
            String request = entrada.readLine();
            String response = null;
            
            while (request != null && !(request.trim().equals("\\sair"))) {
                System.out.println(ipAddress + " - requisitou o comando: " + request);
                try {
                    response = prepareCommand(request);
                } catch (InputMismatchException | NullPointerException e) {
                    response = e.getMessage();
                } finally {
                    client.println(response);
                    System.out.println(ipAddress + " - resposta efetuada.");
                    request = entrada.readLine();
                }
            }

            this.connSocket.close();
        } catch (IOException e) {
        } finally {
            System.out.println("Client " + ipAddress + " desconectado.");
        }
    }

    private String prepareCommand(String request) throws InputMismatchException, NullPointerException {
        request = validateInput(request);
        Map<String, Object> inputFormatted = formatDataEntry(request);
        ICommand comando = ICommand.searchAndReturnCommand((String) inputFormatted.get("command"));

        String param = (String) inputFormatted.get("param");

        if ((boolean) inputFormatted.get("isCommand")) {
            if (param != null) {
                comando.setParam(param);
            }

            return comando.run();
        }

        return comando.usage();
    }

    private String validateInput(String input) throws InputMismatchException {
        input = input.trim();

        boolean startsWithBackslash = input.startsWith("\\");
        boolean startsWithQuestion = input.startsWith("?");

        
        if (!startsWithBackslash && !startsWithQuestion) {
            throw new InputMismatchException(input);
        }

        return input;
    }
    
    private Map<String, Object> formatDataEntry(String input) {
        Map<String, Object> inputFormatted = new HashMap<String, Object>();
        
        if (input.startsWith("\\")) {
            return this.formatDataEntryCommand(input, inputFormatted);
        }

        inputFormatted.put("isCommand", new Boolean(false));
        inputFormatted.put("command", input.replaceAll("\\?", ""));
        
        return inputFormatted;
    }
    
    private Map<String, Object> formatDataEntryCommand(String input, Map<String, Object> inputFormatted) {
        inputFormatted.put("isCommand", new Boolean(true));
        input = input.replaceAll("\\\\", "");
        
        String[] inputWithParams;
        inputWithParams = input.split(" ");
        inputFormatted.put("command", inputWithParams[0]);

        if (inputWithParams.length > 1) {
            inputFormatted.put("param", inputWithParams[1]);
        }

        return inputFormatted;
    }
}