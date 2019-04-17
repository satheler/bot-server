package br.com.satheler.bot.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import br.com.satheler.bot.helpers.APIHelper;
import br.com.satheler.bot.models.Feriado;
import br.com.satheler.bot.providers.CommandProvider;

/**
 * Feriado
 */
public class Feriados extends CommandProvider {

    public Feriados() {
        super(true);
    }

    @Override
    public String run(List<String> params) {
        if(params.isEmpty()) {
            throw new InputMismatchException("Erro de uso. Esperado: " + this.usage());
        }

        String month = params.get(0);

        try {
            APIHelper api = new APIHelper("https://8nwxe02dt5.execute-api.sa-east-1.amazonaws.com/rsd/feriados");
            Map<String, Object> values = new HashMap<String, Object>();
            values.put("month", month);
            String response = api.post(values);
            List<Feriado> feriados = this.jsonToMap(response);
            return this.prepareResponse(feriados);
        } catch(MismatchedInputException e) {
            return "Mês informado inválido";
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    private String prepareResponse(List<Feriado> feriados) {
        if(feriados.isEmpty()){
            return "Este mês não possui feriado";
        }
        String formatted = "";

        for (Feriado feriado : feriados) {
            formatted += feriado.data_ref + " - " + feriado.nome + " - Feriado " + feriado.tipo + "\n";
        }

        return formatted;
    }

	@Override
    public String usage() {
        return super.usage() + " <mes>\t- Retorna todos os feriados do a partir do mês informado.";
    }

    private List<Feriado> jsonToMap(String json) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Feriado> map = mapper.readValue(json, new TypeReference<ArrayList<Feriado>>(){});
        return map;
    }

}
