package it.davincifascetti.controllosocketudp.program;

public interface EventListenerRicezioneBuffer {
    public abstract void update(byte[] buffer,int lung);
}
