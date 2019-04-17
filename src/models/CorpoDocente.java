package models;

import java.io.IOException;

import helpers.APIHelper;

/**
 * CorpoDocente
 */
public class CorpoDocente extends ICommand {

    @Override
    public String run() {
        try {
            APIHelper api = new APIHelper("https://8nwxe02dt5.execute-api.sa-east-1.amazonaws.com/rsd/docentes", "GET");
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