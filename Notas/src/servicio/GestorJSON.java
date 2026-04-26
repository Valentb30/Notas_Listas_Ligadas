package servicio;

import modelo.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.File;
import java.io.FileWriter;

public class GestorJSON {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void guardar(ListaLigada lista, String ruta) {
        try {
            JsonGenerator gen = mapper.getFactory()
                    .createGenerator(new FileWriter(ruta));

            gen.writeStartArray();

            Nodo actual = lista.getCabeza();

            while (actual != null) {
                gen.writeStartObject();

                gen.writeStringField("nota", actual.dato.getNota().name());
                gen.writeStringField("figura", actual.dato.getFigura().name());
                gen.writeNumberField("octava", actual.dato.getOctava());

                gen.writeEndObject();

                actual = actual.siguiente;
            }

            gen.writeEndArray();
            gen.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargar(ListaLigada lista, String ruta) {
        try {
            NotaMusical[] datos = mapper.readValue(
                new File(ruta), NotaMusical[].class
            );

            lista.limpiar();

            for (NotaMusical n : datos) {
                lista.agregar(n);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
