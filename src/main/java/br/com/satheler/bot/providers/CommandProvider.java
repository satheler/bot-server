package br.com.satheler.bot.providers;

import java.util.List;

/**
 * ICommand
 */

public abstract class CommandProvider {

    protected String response;
    private boolean isAsynchronous;

    /**
     * Construtor da classe
     */
    public CommandProvider() {
        this.isAsynchronous = false;
    }

    /**
     * Construtor para inicializar a classe assíncrona ou não assíncrona.
     */
    protected CommandProvider(boolean isAsynchronous) {
        this.isAsynchronous = isAsynchronous;
    }

    /**
     * Método para iniciar a chamada dessa classe como comando.
     * @param params Recebe uma lista com comandos solicitados.
     * @return Resposta final de execução do comando da classe.
     */
    public abstract String run(List<String> params);


    /**
     * Método para retornar informações da classe atual que está sendo chamada.
     * @return Conteúdo com informações do comando da classe. 
     */
    public String usage() {
        return "\\" + ServiceProvider.getOnlyNameClass(this.getClass());
    }

    /**
     * Método para retornar se estado é assíncrono.
     * @return É assíncrono. 
     */
    public boolean isAsynchronous() {
        return this.isAsynchronous;
    }
}
