package models;

import java.io.IOException;

import helpers.APIHelper;

/**
 * CorpoDocente
 */
public class CorpoDocente extends ICommand {

    @Override
    public String run() {
        super.text += "\n====================================================\n";
        super.text += "Aguarde...";

        // try {
        //     APIHelper api = new APIHelper("https://spreadsheets.google.com/feeds/list/1l7Z2zoceNTvaEu_154X0DhAJ0f-0WCqzrZ7m420LF00/6/public/values?alt=json");
        //     this.text += api.getData();
        //     // this.text += api;
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        return this.text;
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna todos os docentes do curso de Engenharia de Software da Unipampa do Campus Alegrete.";
    }

}