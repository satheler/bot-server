package br.com.satheler.bot.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.satheler.bot.helpers.APIHelper;
import br.com.satheler.bot.models.Docente;
import br.com.satheler.bot.providers.CommandProvider;


/**
 * CorpoDocente
 */
public class CorpoDocente extends CommandProvider {

    /**
     * Contrutor para a classe
     */
    public CorpoDocente() {
        super(true);
    }

    /**
     * Método para iniciar a chamada dessa classe como comando.
     * @param params Recebe uma lista com comandos solicitados.
     * @return Resposta final de execução do comando dessa classe.
     */
    @Override
    public String run(List<String> params) {
        try {
            APIHelper api = new APIHelper("https://8nwxe02dt5.execute-api.sa-east-1.amazonaws.com/rsd/docentes");
            String response = api.get();
            List<Docente> docentes = this.jsonToMap(response);
            return this.prepareResponse(docentes);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    /**
     * Método para retornar informações da classe atual que está sendo chamada.
     * @return Conteúdo com informações do comando dessa classe. 
     */
    @Override
    public String usage() {
        return super.usage() + "\t- Retorna todos os docentes do curso de Engenharia de Software da Unipampa do Campus Alegrete.";
    }

    /**
     * Método para fazer conversão do formato Json para um Map.
     * @param json Recebe uma String com os dados do Json para conversão.
     * @return Lista dos Docentes de forma convertida.
     * @throws JsonParseException  Tipo de exceção para problemas de análise, usado quando conteúdo 
     *         que não está em conformidade com a sintaxe JSON conforme especificação é encontrado.
     * @throws JsonMappingException Exceção marcada usada para sinalizar problemas fatais com 
     *         mapeamento de conteúdo.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de falha ou interrupção.
     */
    private List<Docente> jsonToMap(String json) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Docente> map = mapper.readValue(json, new TypeReference<ArrayList<Docente>>(){});
        return map;
    }

    /**
     * Método para preparar a resposta final da requisição dos docentes.
     * @param list Contém todos os resultados de docentes existentes.
     * @return Resultado final dos docentes encontradoss.
     */
    private String prepareResponse(List<Docente> list) {
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
