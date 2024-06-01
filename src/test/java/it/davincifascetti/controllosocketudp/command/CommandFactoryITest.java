package it.davincifascetti.controllosocketudp.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommandFactoryITest {
    @Test
    public void testControllaRegexAssoluta() throws CommandException {
        
        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexAssoluta(null, null);});
        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexAssoluta("ciao", null);});
        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexAssoluta(null, "ciao");});
        assertTrue(CommandFactoryI.controllaRegexAssoluta("ciao", "ciao"));
        assertFalse(CommandFactoryI.controllaRegexAssoluta("ciao", "a"));

        assertFalse(CommandFactoryI.controllaRegexAssoluta("^ciao$", "ciao come va?"));
        assertFalse(CommandFactoryI.controllaRegexAssoluta("ciao", "ciao come va?"));
    }

    @Test
    public void testControllaRegexGruppo() throws CommandException{

        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexGruppo(null, null);});
        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexGruppo("ciao", null);});
        assertThrows(CommandException.class,() -> {CommandFactoryI.controllaRegexGruppo(null, "ciao");});


        assertEquals("errore, non sta restituendo la parte che fa match!","ciao",CommandFactoryI.controllaRegexGruppo("ciao", "ciao"));
        assertEquals("eerrore, non sta restituendo la parte che fa match!","ciao",CommandFactoryI.controllaRegexGruppo("ciao", "ciao come va?"));
        assertEquals("errore, non sta restituendo la parte che fa match!","ciao",CommandFactoryI.controllaRegexGruppo("ciao", "hey, ciao come va?"));
        assertEquals("errore, non sta restituendo la parte che fa match!","ciao ",CommandFactoryI.controllaRegexGruppo("ciao ", "hey, ciao come va?"));
        assertEquals("errore, non sta restituendo la parte che fa match!",null,CommandFactoryI.controllaRegexGruppo("ciao ", "hey, come va?"));
        assertEquals("errore, non sta restituendo la parte che fa match!",null,CommandFactoryI.controllaRegexGruppo("^ciao$", "hey, ciao come va?"));
    }

    
}
