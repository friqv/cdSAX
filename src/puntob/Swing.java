package puntob;

import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Swing {
    static final String NOMBRE_CLASE = Swing.class.getSimpleName();
    static final Logger LOG = Logger.getLogger(NOMBRE_CLASE);

    private JFrame frame;  //Ventana Principal
    private JTable tabla; //TABLA DE LOS DATOS XML
    private DefaultTableModel modeloTabla; //MODELO VISTO EN FUNDAMENTOS DE COMPU III
    private JComboBox<String> AlmacenXML; //LISTA A ELEGIR DE LOS XML ALMACENADOS
    //PERMITE SELECCIONAR UN STRING DE UNA LISTA



    //GUARDAR LOS XML EN ALMACEN_XML PARA PODER EJECUTARLOS
    public Swing() {
        //CREACIÓN DEL FRAME
        frame = new JFrame("XML");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        AlmacenXML = new JComboBox<>(); //OBJETO DE LA LISTA
        AlmacenXML.addActionListener(new ActionListener() {
            @Override //HILO
            public void actionPerformed(ActionEvent e) {
                cargarArchivoXML((String) AlmacenXML.getSelectedItem());
            }
        });

        //PANEL SUPERIOR
        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("ELIGE EL XML QUE QUIERAS USAR"));
        panelSuperior.add(AlmacenXML);

        frame.add(panelSuperior, BorderLayout.NORTH); //VISUALIZACIÓN

        modeloTabla = new DefaultTableModel();  //MODELO DE LA TABLITA
        tabla = new JTable(modeloTabla);
        JScrollPane panelDesplazamiento = new JScrollPane(tabla);
        frame.add(panelDesplazamiento, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);

        //LISTA DE LOS XML ALMACENADADOS EN ALMACEN_XML
        List<String> archivosXML = obtenerArchivosXMLDisponibles();
        for (String archivoXML : archivosXML) {
            AlmacenXML.addItem(archivoXML);
        }
    }
        //METODO PARA TRABJAR CON LOS XML ALMACENADOS
    private List<String> obtenerArchivosXMLDisponibles() {
        File carpeta = new File("C:\\\\Users\\\\CM\\\\IdeaProjects\\\\cdSAX\\\\src\\\\ALMACEN_XML\\\\");
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".xml"));

        List<String> archivosXML = new ArrayList<>();
        if (archivos != null) {
            for (File archivo : archivos) {
                archivosXML.add(archivo.getName());
            }
        }
        return archivosXML;
    }
    //CARGAR EL XML
    private void cargarArchivoXML(String nombreArchivo) {
        modeloTabla.setRowCount(0);
        modeloTabla.setColumnCount(0);

        try {
            File archivoXML = new File("C:\\\\Users\\\\CM\\\\IdeaProjects\\\\cdSAX\\\\src\\\\ALMACEN_XML\\\\" + nombreArchivo);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(archivoXML);

            doc.getDocumentElement().normalize();
            Element raiz = doc.getDocumentElement();

            NodeList nodos = raiz.getChildNodes();

            for (int i = 0; i < nodos.getLength(); i++) {
                Node nodo = nodos.item(i);
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    if (modeloTabla.getColumnCount() == 0) {
                        //ENCABEZADO
                        NodeList nodosHijos = nodo.getChildNodes();
                        for (int j = 0; j < nodosHijos.getLength(); j++) {
                            Node nodoHijo = nodosHijos.item(j);
                            if (nodoHijo.getNodeType() == Node.ELEMENT_NODE) {
                                modeloTabla.addColumn(nodoHijo.getNodeName());
                            }
                        }
                    }

                    //ACTUALIZAMOS LA FILA
                    Object[] filaDatos = new Object[modeloTabla.getColumnCount()];
                    NodeList nodosHijos = nodo.getChildNodes();
                    int indiceColumna = 0;
                    for (int j = 0; j < nodosHijos.getLength(); j++) {
                        Node nodoHijo = nodosHijos.item(j);
                        if (nodoHijo.getNodeType() == Node.ELEMENT_NODE) {
                            filaDatos[indiceColumna++] = nodoHijo.getTextContent();
                        }
                    }
                    modeloTabla.addRow(filaDatos);
                }
            }
        } catch (Exception e) {
            LOG.severe(e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { //HILO SEPARADO POR EVENTOS, TRABAJAMDO CON INTERFACES SWING
            new Swing();
        });
    }
}
