package models;

import app.Application;

/**
 * Ajuda
 */
public class Comandos extends ICommand {

    @Override
    public String run() {
        super.text += "\n====================================================\n\n";
        super.text += "Comandos disponiveis: \n";
        for (ICommand command : Application.availableCommands) {
            if(command.usage() != null) {
                super.text += "\t" + command.usage() + "\n";
            }
        }
        super.text += "\n====================================================\n";

        return super.text;
        // System.out.println("Comando teste chamado");
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna os comandos disponiveis.";
    }

}
        
       
        