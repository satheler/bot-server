package br.com.satheler.bot.providers;

import java.util.List;

/**
 * ICommand
 */

public abstract class CommandProvider {

    protected String response;
    private boolean isAsynchronous;

    public CommandProvider() {
        this.isAsynchronous = false;
    }

    protected CommandProvider(boolean isAsynchronous) {
        this.isAsynchronous = isAsynchronous;
    }

    public abstract String run(List<String> params);

    public String usage() {
        return "\\" + ServiceProvider.getOnlyNameClass(this.getClass());
    }

    public boolean isAsynchronous() {
        return this.isAsynchronous;
    }
}
