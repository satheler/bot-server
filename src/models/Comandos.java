package models;

import app.Application;

/**
 * Ajuda
 */
public class Comandos extends ICommand {

    @Override
    public String run() {
        super.text = "Comandos disponiveis:";
        for (ICommand command : Application.AVAILABLE_COMMANDS) {
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
        
       
        