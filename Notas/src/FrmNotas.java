import modelo.*;
import servicio.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FrmNotas extends JFrame {

    private ListaLigada lista = new ListaLigada();

    private JComboBox<Nota> comboNota = new JComboBox<>(Nota.values());
    private JComboBox<Figura> comboFigura = new JComboBox<>(Figura.values());
    private JTextField txtOctava = new JTextField("4");

    private DefaultTableModel modeloTabla;
    private JTable tabla;

    private int filaSeleccionada = -1;

    public FrmNotas() {

        setTitle("Editor de Notas");
        setSize(600, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        comboNota.setBounds(20, 20, 120, 25);
        comboFigura.setBounds(150, 20, 120, 25);
        txtOctava.setBounds(280, 20, 50, 25);

        JLabel lblAgregar = new JLabel(new ImageIcon(getClass().getResource("/img/agregar.png")));
        lblAgregar.setBounds(350, 10, 50, 50);

        JLabel lblActualizar = new JLabel(new ImageIcon(getClass().getResource("/img/editar.png")));
        lblActualizar.setBounds(410, 10, 50, 50);

        JLabel lblEliminar = new JLabel(new ImageIcon(getClass().getResource("/img/eliminar.png")));
        lblEliminar.setBounds(20, 270, 50, 50);

        JLabel lblReproducir = new JLabel(new ImageIcon(getClass().getResource("/img/play.png")));
        lblReproducir.setBounds(80, 270, 50, 50);

        JLabel lblGuardar = new JLabel(new ImageIcon(getClass().getResource("/img/guardar.png")));
        lblGuardar.setBounds(140, 270, 50, 50);

        JLabel lblCargar = new JLabel(new ImageIcon(getClass().getResource("/img/cargar.png")));
        lblCargar.setBounds(200, 270, 50, 50);

        modeloTabla = new DefaultTableModel(
                new Object[] { "Nota", "Figura", "Octava" }, 0);

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 70, 540, 180);

        // AGREGAR COMPONENTES
        add(comboNota);
        add(comboFigura);
        add(txtOctava);
        add(lblAgregar);
        add(lblActualizar);
        add(scroll);
        add(lblEliminar);
        add(lblReproducir);
        add(lblGuardar);
        add(lblCargar);

        lblAgregar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    Nota n = (Nota) comboNota.getSelectedItem();
                    Figura f = (Figura) comboFigura.getSelectedItem();
                    int o = Integer.parseInt(txtOctava.getText());

                    lista.agregar(new NotaMusical(n, f, o));
                    actualizarTabla();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar");
                }
            }
        });

        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila != -1) {
                filaSeleccionada = fila;

                comboNota.setSelectedItem(tabla.getValueAt(fila, 0));
                comboFigura.setSelectedItem(tabla.getValueAt(fila, 1));
                txtOctava.setText(tabla.getValueAt(fila, 2).toString());
            }
        });

        lblActualizar.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una fila");
                    return;
                }

                try {
                    Nota n = (Nota) comboNota.getSelectedItem();
                    Figura f = (Figura) comboFigura.getSelectedItem();
                    int o = Integer.parseInt(txtOctava.getText());

                    modificarNodo(filaSeleccionada, new NotaMusical(n, f, o));

                    actualizarTabla();
                    filaSeleccionada = -1;

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar");
                }
            }
        });

        lblEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int fila = tabla.getSelectedRow();

                if (fila == -1) {
                    JOptionPane.showMessageDialog(null, "Selecciona una fila");
                    return;
                }

                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Estás seguro de eliminar esta nota?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (opcion == JOptionPane.YES_OPTION) {

                    lista.eliminar(fila);
                    actualizarTabla();

                    JOptionPane.showMessageDialog(null, "Nota eliminada");

                } else {
                    // Opcional
                    System.out.println("Eliminación cancelada");
                }
            }
        });

        lblReproducir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {

                // 🔥 VALIDACIÓN
                if (lista.getCabeza() == null) {
                    JOptionPane.showMessageDialog(null,
                            "No hay notas para reproducir",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Nodo actual = lista.getCabeza();

                ReproductorAudioMIDI.iniciar();

                while (actual != null) {
                    ReproductorAudioMIDI.reproducir(actual.dato);
                    actual = actual.siguiente;
                }

                ReproductorAudioMIDI.cerrar();
            }
        });

        lblGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {

                // 🔥 VALIDACIÓN
                if (lista.getCabeza() == null) {
                    JOptionPane.showMessageDialog(null,
                            "No hay notas para guardar");
                    return;
                }

                JFileChooser fc = new JFileChooser();

                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    String ruta = fc.getSelectedFile().getAbsolutePath();
                    if (!ruta.endsWith(".json"))
                        ruta += ".json";

                    GestorJSON.guardar(lista, ruta);

                    JOptionPane.showMessageDialog(null, "Archivo guardado");
                }
            }
        });

        lblCargar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Seleccionar archivo JSON");

                fileChooser.setFileFilter(
                        new javax.swing.filechooser.FileNameExtensionFilter(
                                "Archivos JSON (*.json)", "json"));

                fileChooser.setAcceptAllFileFilterUsed(false);

                int resultado = fileChooser.showOpenDialog(null);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    try {
                        String ruta = fileChooser.getSelectedFile().getAbsolutePath();

                        if (!ruta.toLowerCase().endsWith(".json")) {
                            JOptionPane.showMessageDialog(null, "Selecciona un archivo JSON válido");
                            return;
                        }

                        GestorJSON.cargar(lista, ruta);
                        actualizarTabla();

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error al cargar archivo");
                    }
                }
            }
        });

    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        Nodo actual = lista.getCabeza();

        while (actual != null) {
            modeloTabla.addRow(new Object[] {
                    actual.dato.getNota(),
                    actual.dato.getFigura(),
                    actual.dato.getOctava()
            });

            actual = actual.siguiente;
        }
    }

    private void modificarNodo(int indice, NotaMusical nueva) {

        Nodo actual = lista.getCabeza();
        int i = 0;

        while (actual != null) {
            if (i == indice) {
                actual.dato = nueva;
                return;
            }
            actual = actual.siguiente;
            i++;
        }
    }
}
