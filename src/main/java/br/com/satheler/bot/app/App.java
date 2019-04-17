package br.com.satheler.bot.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import br.com.satheler.bot.models.ICommand;

/**
 * Hello world!
 */
public final class App {
    public static List<ICommand> AVAILABLE_COMMANDS;
    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        AVAILABLE_COMMANDS = ICommand.searchCommands();
        int port = 1234;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Servidor rodando na porta " + port);
            while (true) {
                Socket conexao = serverSocket.accept();
                Thread newThread = new Server(conexao);
                newThread.start();
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }
}
