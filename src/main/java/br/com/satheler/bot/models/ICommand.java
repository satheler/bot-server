package br.com.satheler.bot.models;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import br.com.satheler.bot.app.App;
import br.com.satheler.bot.helpers.ReflectionHelper;

/**
 * ICommand
 */

public abstract class ICommand {

    protected String text = "";
    protected String param;

    /**
     * Este método é chamado para rodar o comando requerido
     *
     * @return void
     * @throws IOException
     * @throws MalformedURLException
     */
    public abstract String run();

    public void setParam(String param) {
        this.param = param;
    }

    /**
     * Este método é chamado caso ele seja usado de maneira errada
     * ou quando o usuário quiser saber o que os comandos fazem
     *
     * @return Dica de uso do comando
     */
    public String usage() {
        return "\\" + getOnlyNameClass(this.getClass().getName());
    }

    private static String getOnlyNameClass(String nameClass) {
        return (nameClass.split("\\.")[nameClass.split("\\.").length - 1]).toLowerCase();
    }

    public static ICommand searchAndReturnCommand(String commandString) {
        for (ICommand command : App.AVAILABLE_COMMANDS) {
            String nameClass = command.getClass().getName();
            String onlyNameClass = getOnlyNameClass(nameClass);

            if (onlyNameClass.equalsIgnoreCase(commandString)) {
                return command;
            }
        }

        throw new NullPointerException("Comando nao encontrado. Para ver os comandos disponíveis utilize: \\comandos");
    }

    public static List<ICommand> searchCommands()
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<Class<ICommand>> findCommands = ReflectionHelper.findClassesImplementing(ICommand.class);
        List<ICommand> commands = new ArrayList();

        for (Class<ICommand> command : findCommands) {
            Class cls = Class.forName(command.getName());
            ICommand found = (ICommand) cls.newInstance();
            commands.add(found);
        }

        return commands;
    }
}
