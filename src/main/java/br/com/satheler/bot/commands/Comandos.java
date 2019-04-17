package br.com.satheler.bot.commands;

import br.com.satheler.bot.app.App;

/**
 * Ajuda
 */
public class Comandos extends ICommand {

    @Override
    public String run() {
        super.text = "Comandos disponiveis:";
        for (ICommand command : App.AVAILABLE_COMMANDS) {
            if(command.usage() != null) {
                super.text += "\n\t" + command.usage();
            }
        }

        return super.text;
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna os comandos disponiveis.";
    }

}


