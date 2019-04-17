package br.com.satheler.bot.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.satheler.bot.models.Docente;
import br.com.satheler.bot.helpers.APIHelper;


/**
 * CorpoDocente
 */
public class CorpoDocente extends ICommand {

    @Override
    public String run() {
        try {
            APIHelper api = new APIHelper("https://8nwxe02dt5.execute-api.sa-east-1.amazonaws.com/rsd/docentes", "GET");
            String response = api.response();
            List<Docente> docentes = this.jsonToMap(response);
            this.text = this.prepareString(docentes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.text;
    }

    @Override
    public String usage() {
        return super.usage() + "\t- Retorna todos os docentes do curso de Engenharia de Software da Unipampa do Campus Alegrete.";
    }

    private List<Docente> jsonToMap(String json) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Docente> map = mapper.readValue(json, new TypeReference<ArrayList<Docente>>(){});
        return map;
    }

    private String prepareString(List<Docente> list) {
        String formatted = "";
        for (Docente docente : list) {
            if(docente.hasDoutorado) {
                formatted += "Dr. ";
            }

            if(docente.hasMestrado) {
                formatted += "Me. ";
            }

            formatted += docente.nome + "\n";
        }

        return formatted;
    }

}
