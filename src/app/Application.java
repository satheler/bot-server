package app;

// import java.io.ObjectOutputStream;
// import java.net.ServerSocket;
// import java.net.Socket;
// import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import models.ICommand;

public class Application {

    public static List<ICommand> availableCommands;

    public static void main(String[] args)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        availableCommands = ICommand.searchCommands();
        Scanner scanner = new Scanner(System.in);
        String entrada;
        ICommand comando;

        do {
            System.out.print("Insira o comando: ");
            entrada = scanner.nextLine();
            try {
                entrada = validateInput(entrada);
                comando = ICommand.searchAndReturnCommand(entrada);
                System.out.println(comando.run());
            } catch (InputMismatchException | NullPointerException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private static String validateInput(String input) throws InputMismatchException {
        boolean startsWith = input.startsWith("\\");

        if (!startsWith) {
            throw new InputMismatchException(input);
        }

        input = input.replaceAll("\\\\", "");
        return input;
    }
}