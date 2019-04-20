package br.com.satheler.bot.commands;

import java.util.List;

import br.com.satheler.bot.providers.CommandProvider;
import br.com.satheler.bot.providers.ServiceProvider;

/**
 * Ajuda
 */
public class Comandos extends CommandProvider {

    /**
     * Método para iniciar a chamada dessa classe como comando.
     * @param params Recebe uma lista com comandos solicitados.
     * @return Resposta final de execução do comando dessa classe.
     */
    @Override
    public String run(List<String> params) {
        response = "Comandos disponiveis:";
        for (CommandProvider command : ServiceProvider.AVAILABLE_COMMANDS) {
            if (command.usage() != null) {
                response += "\n\t" + command.usage();
            }
        }

        return response;
    }

    /**
     * Método para retornar informações da classe atual que está sendo chamada.
     * @return Conteúdo com informações do comando dessa classe. 
     */
    @Override
    public String usage() {
        return super.usage() + "\t- Retorna os comandos disponiveis.";
    }
}
