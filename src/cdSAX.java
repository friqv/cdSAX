import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class cdSAX extends DefaultHandler {

    private static final String CLASS_NAME = DisplayXML.class.getName();
    private final static Logger LOG = Logger.getLogger(CLASS_NAME);

    private boolean TITLE;
    private boolean ARTIST;
    private boolean COUNTRY;
    private boolean COMPANY;
    private boolean PRICE;
    private boolean YEAR;

    private CD fooItem;
    private ArrayList<CD> lista;

    public cdSAX() {
        super();

        lista = new ArrayList<>();
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            LOG.severe("No file to process. Usage is:" + "\njava BreakFastHandler <filename>");
            return;
        }

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);

        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            LOG.severe(e.getMessage());
        } catch (SAXException e) {
            LOG.severe(e.getMessage());
        }

        File xmlFile = new File(args[0]);

        cdSAX handler = new cdSAX();

        try {
            saxParser.parse(xmlFile, handler);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

        ArrayList<CD> lista = handler.getLista();
        ArrayList<Double> listaDePrecios = new ArrayList<>();

        for (CD cd: lista) {
            double precio = cd.getPRICE();
            listaDePrecios.add(precio);
            //System.out.println(cd.toString());
        }

        double desviacionEstandar =  calcularDesviacionEstandar(listaDePrecios);

        System.out.println("Precios de los discos");
        for (int i = 0; i < listaDePrecios.size(); i++) {
            double precio = listaDePrecios.get(i);
            System.out.println( "Disco " + (i + 1) + ". Precio: " + precio);
        }
        System.out.println("\nDesviación estándar de la muestra: " + desviacionEstandar);

    }

    private static double calcularDesviacionEstandar(ArrayList<Double> listaDePrecios) {
        // Calcula la media (promedio)
        double media = listaDePrecios.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        // Calcula la suma de las diferencias al cuadrado
        double sumaDiferenciasCuadrado = listaDePrecios.stream()
                .mapToDouble(valor -> Math.pow(valor - media, 2))
                .sum();

        // Calcula la varianza (media de las diferencias al cuadrado)
        double varianza = sumaDiferenciasCuadrado / listaDePrecios.size();

        // Calcula la desviación estándar (raíz cuadrada de la varianza)
        return Math.sqrt(varianza);
    }



    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        //LOG.info("startElement");

        if (localName.equals("CD")) {
            fooItem = new CD();
        }
        if (localName.equals("TITLE"))
            TITLE = true;
        if (localName.equals("ARTIST"))
            ARTIST = true;
        if (localName.equals("COUNTRY"))
            COUNTRY = true;
        if (localName.equals("COMPANY"))
            COMPANY= true;
        if (localName.equals("PRICE"))
            PRICE = true;
        if (localName.equals("YEAR"))
            YEAR= true;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // LOG.info("characters");
        String contenido = new String(ch, start, length);

        if (TITLE) {
            fooItem.setTITLE(contenido);
        } else if (ARTIST) {
            fooItem.setARTIST(contenido);
        } else if (COUNTRY) {
            fooItem.setCOUNTRY(contenido);
        } else if (COMPANY) {
            fooItem.setCOMPANY(contenido);
        } else if (PRICE) {
            fooItem.setPRICE(Double.parseDouble(contenido));
        } else if (YEAR) {
            fooItem.setYEAR(Integer.parseInt(contenido));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        //LOG.info( qName );
        if (localName.equals("TITLE"))
            TITLE = false;
        if (localName.equals("ARTIST"))
            ARTIST = false;
        if (localName.equals("COUNTRY"))
            COUNTRY = false;
        if (localName.equals("COMPANY"))
            COMPANY = false;
        if (localName.equals("PRICE"))
            PRICE = false;
        if (localName.equals("YEAR"))
            YEAR = false;

        if (localName.equals("CD")) {
            //System.out.println( fooItem.toString() ) ;
            lista.add(fooItem);
        }
    }

    public ArrayList<CD> getLista() {
        return lista;
    }

}
