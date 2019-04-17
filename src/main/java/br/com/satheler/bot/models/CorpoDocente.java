package br.com.satheler.bot.models;

import java.io.IOException;

import br.com.satheler.bot.helpers.APIHelper;


/**
 * CorpoDocente
 */
public class CorpoDocente extends ICommand {

    @Override
    public String run() {
        try {
            APIHelper api = new br.com.satheler.bot.helpers.APIHelper("https://8nwxe02dt5.execute-api.sa-east-1.amazonaws.com/rsd/docentes", "GET");
            this.text = api.response();
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return this.text;
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna todos os docentes do curso de Engenharia de Software da Unipampa do Campus Alegrete.";
    }

}
