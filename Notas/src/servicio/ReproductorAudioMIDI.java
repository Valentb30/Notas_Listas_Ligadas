package servicio;

import modelo.*;
import javax.sound.midi.*;

public class ReproductorAudioMIDI {

    private static final int[] NOTAS_MIDI = {
        60,61,62,63,64,65,66,67,68,69,70,71
    };

    private static Synthesizer synth;
    private static MidiChannel canal;

    public static void iniciar() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            canal = synth.getChannels()[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reproducir(NotaMusical nota) {
        try {
            int valor = NOTAS_MIDI[nota.getNota().ordinal()] + (nota.getOctava() - 4) * 12;

            int duracion = obtenerDuracion(nota.getFigura());

            canal.noteOn(valor, 100);
            Thread.sleep(duracion);
            canal.noteOff(valor);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int obtenerDuracion(Figura figura) {
        switch (figura) {
            case REDONDA:
                return 2000;
            case BLANCA:
                return 1000;
            case NEGRA:
                return 500;
            case CORCHEA:
                return 300;
            default:
                return 400;
        }
    }

    public static void cerrar() {
        synth.close();
    }
}
