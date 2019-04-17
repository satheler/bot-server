package br.com.satheler.bot.commands;

import java.util.List;

import br.com.satheler.bot.providers.CommandProvider;
import br.com.satheler.bot.providers.ServiceProvider;

/**
 * Ajuda
 */
public class Comandos extends CommandProvider {

    @Override
    public String run(List<String> params) {
        response = "Comandos disponiveis:";
        for (CommandProvider command : ServiceProvider.AVAILABLE_COMMANDS) {
            if (command.usage() != null) {
                response += "\n\t" + command.usage();
            }
        }

        return response;
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna os comandos disponiveis.";
    }
}
