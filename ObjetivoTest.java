package br.com.satheler.bot.commands;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * ObjetivoTest
 */
public class ObjetivoTest {

    private Objetivo command;

    @Before
    public void setUp() {
        command = new Objetivo();
    }

    @Test
    public void runCommand() {
        assertNotNull(command);
    }
}
