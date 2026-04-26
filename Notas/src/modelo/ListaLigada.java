package modelo;

public class ListaLigada {

    private Nodo cabeza;

    public void agregar(NotaMusical nota) {
        Nodo nuevo = new Nodo(nota);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    public void eliminar(int indice) {
        if (cabeza == null) return;

        if (indice == 0) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo actual = cabeza;
        int i = 0;

        while (actual.siguiente != null) {
            if (i + 1 == indice) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
            i++;
        }
    }

    public String mostrar() {
        StringBuilder sb = new StringBuilder();
        Nodo actual = cabeza;
        int i = 0;

        while (actual != null) {
            sb.append(i).append(": ").append(actual.dato).append("\n");
            actual = actual.siguiente;
            i++;
        }
        return sb.toString();
    }

    public Nodo getCabeza() { return cabeza; }

    public void limpiar() { cabeza = null; }
}
