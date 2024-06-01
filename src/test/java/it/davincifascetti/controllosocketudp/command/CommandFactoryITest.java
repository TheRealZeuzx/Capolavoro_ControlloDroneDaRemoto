package it.davincifascetti.controllosocketudp.command;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommandFactoryITest {
    @Test
    public void testControllaRegexAssoluta() throws CommandException {
        assertTrue(CommandFactoryI.controllaRegexAssoluta("null", "null"));
    }

    @Test
    public void testControllaRegexGruppo() {

    }
}
