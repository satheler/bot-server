package br.com.satheler.bot.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import br.com.satheler.bot.helpers.ReflectionHelper;

/**
 * Helper
 */
public class ServiceProvider {

    private static ServiceProvider instance;
    public static List<CommandProvider> AVAILABLE_COMMANDS;
    public static CommandProvider CURRENT_COMMAND;

    private ServiceProvider() {
        AVAILABLE_COMMANDS = this.searchCommands();
    }

    private List<CommandProvider> searchCommands() {
        List<Class<CommandProvider>> findCommands = ReflectionHelper.findClassesImplementing(CommandProvider.class, "br.com.satheler.bot.commands");
        List<CommandProvider> commands = new ArrayList<CommandProvider>();

        for (Class<CommandProvider> command : findCommands) {
            Class cls;
            try {
                cls = Class.forName(command.getName());
                CommandProvider found = (CommandProvider) cls.newInstance();
                commands.add(found);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return commands;
    }

    public static CommandProvider searchAndReturnCommand(String commandString) {
        for (CommandProvider command : AVAILABLE_COMMANDS) {
            Class nameClass = command.getClass();
            String onlyNameClass = getOnlyNameClass(nameClass);

            if (onlyNameClass.equalsIgnoreCase(commandString)) {
                return command;
            }
        }

        throw new NullPointerException("Comando nao encontrado. Para ver os comandos disponíveis utilize: \\comandos");
    }

    public static Map<String, Object> prepareRequest(String request) throws InputMismatchException, NullPointerException {
        request = validateInput(request);
        Map<String, Object> inputFormatted = formatDataEntry(request);
        CURRENT_COMMAND = searchAndReturnCommand((String) inputFormatted.get("command"));

        return inputFormatted;
    }

    public static boolean commandIsAsynchronous() {
        return CURRENT_COMMAND.isAsynchronous();
    }

    public static String runRequest(Map<String, Object> input) {
        List<String> param = (List<String>) input.get("param");

        if ((boolean) input.get("isCommand")) {
            return CURRENT_COMMAND.run(param);
        }

        return CURRENT_COMMAND.usage();
    }

    private static String validateInput(String input) throws InputMismatchException {
        input = input.trim();

        boolean startsWithBackslash = input.startsWith("\\");
        boolean startsWithQuestion = input.startsWith("?");

        if (!startsWithBackslash && !startsWithQuestion) {
            throw new InputMismatchException(input);
        }

        return input;
    }

    private static Map<String, Object> formatDataEntry(String input) {
        Map<String, Object> inputFormatted = new HashMap<String, Object>();

        if (input.startsWith("\\")) {
            return formatDataEntryCommand(input, inputFormatted);
        }

        inputFormatted.put("isCommand", new Boolean(false));
        inputFormatted.put("command", input.replaceAll("\\?", ""));

        return inputFormatted;
    }

    private static Map<String, Object> formatDataEntryCommand(String input, Map<String, Object> inputFormatted) {
        inputFormatted.put("isCommand", new Boolean(true));
        input = input.replaceAll("\\\\", "");

        String[] inputWithParams;
        inputWithParams = input.split(" ");
        inputFormatted.put("command", inputWithParams[0]);

        List<String> params = new ArrayList<String>();
        if (inputWithParams.length > 1) {
            for (int i = 1; i < inputWithParams.length; i++) {
                params.add(inputWithParams[i]);
            }
        }
        inputFormatted.put("param", params);

        return inputFormatted;
    }


    public static <T> String getOnlyNameClass(Class<T> classReference) {
        String nameClass = classReference.getName();
        return (nameClass.split("\\.")[nameClass.split("\\.").length - 1]).toLowerCase();
    }

    public static ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }

        return instance;
    }
}
