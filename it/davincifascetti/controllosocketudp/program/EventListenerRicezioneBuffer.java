package it.davincifascetti.controllosocketudp.program;

import it.davincifascetti.controllosocketudp.command.Commandable;

public interface EventListenerRicezioneBuffer {
    public abstract void update(byte[] buffer,int lung,Commandable commandable);
}
