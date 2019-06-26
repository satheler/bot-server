package br.com.satheler.bot.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import br.com.satheler.bot.commands.Comandos;
import br.com.satheler.bot.commands.CorpoDocente;
import br.com.satheler.bot.commands.Feriados;
import br.com.satheler.bot.commands.Objetivo;
import br.com.satheler.bot.helpers.ReflectionHelper;

/**
 * Helper
 */
public class ServiceProvider {

    private static ServiceProvider instance;
    public static List<CommandProvider> AVAILABLE_COMMANDS;
    public static CommandProvider CURRENT_COMMAND;

    private ServiceProvider() {
        // AVAILABLE_COMMANDS = this.searchCommands();
        AVAILABLE_COMMANDS = new ArrayList<CommandProvider>();
        AVAILABLE_COMMANDS.add(new Comandos());
        AVAILABLE_COMMANDS.add(new CorpoDocente());
        AVAILABLE_COMMANDS.add(new Feriados());
        AVAILABLE_COMMANDS.add(new Objetivo());
    }

    /**
     * Método para pesquisar comandos disponíveis para o servidor.
     * @return List<CommandProvider> comandos disponíveis
     */
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

    /**
     * Método para pesquisar e retornar um comando a partir de uma entrada com String.
     * @param commandString Recebe nome de comando para pesquisa.
     * @return Provedor de Comando.
     */
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

    /**
     * Método para preparar a requisição
     * @param request recebe uma String com o pedido da requisição.
     * @return Mapa formatado com o pedido convertido
     * @throws InputMismatchException Lançado por um Scanner para indicar que o token recuperado
     *         não corresponde ao padrão para o tipo esperado ou que o token está fora do intervalo
     *         para o tipo esperado.
     * @throws NullPointerException Lançada quando um aplicativo tenta usar alguma variável/objeto nulo
     *         em um caso.
     */
    public static Map<String, Object> prepareRequest(String request) throws InputMismatchException, NullPointerException {
        request = validateInput(request);
        Map<String, Object> inputFormatted = formatDataEntry(request);
        CURRENT_COMMAND = searchAndReturnCommand((String) inputFormatted.get("command"));

        return inputFormatted;
    }

    /**
     * Método para verificar se o comando é assíncrono.
     * @return Se o comando é assíncrono.
     */
    public static boolean commandIsAsynchronous() {
        return CURRENT_COMMAND.isAsynchronous();
    }

    /**
     * Método para iniciar o pedido para a requisição.
     * @param input mapa com os valores de entrada para iniciar o pedido.
     * @return Comando solicitado.
     */
    public static String runRequest(Map<String, Object> input) {
        List<String> param = (List<String>) input.get("param");

        if ((boolean) input.get("isCommand")) {
            return CURRENT_COMMAND.run(param);
        }

        return CURRENT_COMMAND.usage();
    }

    /**
     * Método para realizar a validação do valor da entrada.
     * @param input Recebe a entrada para ser verificado.
     * @return Entrada validada.
     * @throws InputMismatchException Lançado por um Scanner para indicar que o token recuperado
     *         não corresponde ao padrão para o tipo esperado ou que o token está fora do intervalo
     *         para o tipo esperado.
     */
    private static String validateInput(String input) throws InputMismatchException {
        input = input.trim();

        boolean startsWithBackslash = input.startsWith("\\");
        boolean startsWithQuestion = input.startsWith("?");

        if (!startsWithBackslash && !startsWithQuestion) {
            throw new InputMismatchException(input);
        }

        return input;
    }

    /**
     * Método para realizar a validação dos dados de entrada.
     * @param input Recebe os valores dos dados para serem validados.
     * @return Uma mapa com a validação dos dados de entrada.
     */
    private static Map<String, Object> formatDataEntry(String input) {
        Map<String, Object> inputFormatted = new HashMap<String, Object>();

        if (input.startsWith("\\")) {
            return formatDataEntryCommand(input, inputFormatted);
        }

        inputFormatted.put("isCommand", new Boolean(false));
        inputFormatted.put("command", input.replaceAll("\\?", ""));

        return inputFormatted;
    }

    /**
     * Método para realizar a formatação do comando de entrada dos Dados.
     * @param input Recebe os valores dos dados para serem validados.
     * @param inputFormatted Recebe os valores de entradas formatados.
     * @return Uma mapa com a validação dos comandos de entrada dos dados.
     */
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

    /**
     * Método para retorna apenas o nome de uma classe dado a sua instância.
     * @param <T> (GENÉRICO *DO TIPO T*).
     * @param classReference Recebe a referência da classe.
     * @return Nome da classe dado a sua instância.
     */
    public static <T> String getOnlyNameClass(Class<T> classReference) {
        String nameClass = classReference.getName();
        return (nameClass.split("\\.")[nameClass.split("\\.").length - 1]).toLowerCase();
    }

    /**
     * Método para recuperar instância da classe.
     */
    public static ServiceProvider getInstance() {
        if (instance == null) {
            instance = new ServiceProvider();
        }

        return instance;
    }
}
