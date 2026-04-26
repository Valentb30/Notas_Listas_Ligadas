package modelo;

public class Nodo {
    public NotaMusical dato;
    public Nodo siguiente;

    public Nodo(NotaMusical dato) {
        this.dato = dato;
        this.siguiente = null;
    }
}
