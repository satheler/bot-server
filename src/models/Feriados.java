package models;

/**
 * Feriado
 */
public class Feriados extends ICommand {

    @Override
    public String run() {
        if(this.param == null) {
            return "Erro de uso. Esperado: " + this.usage();
        }
        
        return "NÃO IMPLEMENTADO, BUT I HAVE PARAM: " + this.param;
    }

    @Override
    public String usage() {
        return super.usage() + " <mes>\t- Retorna todos os feriados do a partir do mês informado.";
    }

}