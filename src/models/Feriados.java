package models;

/**
 * Feriado
 */
public class Feriados extends ICommand {

    @Override
    public String run() {
        return "NÃO IMPLEMENTADO";
    }

    @Override
    public String usage() {
        return super.usage() + " <mes>\t- Retorna todos os feriados do a partir do mês informado.";
    
    }

}