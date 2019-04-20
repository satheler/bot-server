package br.com.satheler.bot.commands;

import java.util.List;
import java.util.Map;

import br.com.satheler.bot.providers.CommandProvider;

/**
 * Objetivo
 */
public class Objetivo extends CommandProvider {

    /**
     * Método para iniciar a chamada dessa classe como comando.
     * @param params Recebe uma lista com comandos solicitados.
     * @return Resposta final de execução do comando dessa classe.
     */
    @Override
    public String run(List<String> params) {
        response = "O Curso de Engenharia de Software, comprometido em concretizar a missão institucional da Unipampa, tem como objetivo geral promover ensino, pesquisa e extensão em Engenharia de Software, contribuindo com o desenvolvimento sustentável da região e do país. Complementarmente, o curso tem os seguintes objetivos específicos: \n";

        response += "\t- formar profissionais qualificados, éticos e aptos a atuar em diferentes atividades da área de Engenharia de Software;\n";
        response += "\t- capacitar os discentes a aplicar seus conhecimentos de forma empreendedora e inovadora, contribuindo com o desenvolvimento humano, econômico e social;\n";
        response += "\t- propiciar experiências de aprendizado para que os discentes desenvolvam autonomia no que diz respeito a resolução de problemas, trabalho em equipe, tomada de decisões e capacidade de comunicação; \n";
        response += "\t- evoluir o estado da arte e o estado da prática em Engenharia de Software por meio da pesquisa teórica e aplicada;\n";
        response += "\t- promover a interação e a troca de saberes e experiências entre a comunidade acadêmica e a sociedade por meio da extensão universitária.";

        return response;
    }

    /**
     * Método para retornar informações da classe atual que está sendo chamada.
     * @return Conteúdo com informações do comando dessa classe. 
     */
    @Override
    public String usage() {
        return super.usage() + "\t- Retorna qual o objetivo do Curso de Engenharia de Software segundo o PPC.";
    }
}
