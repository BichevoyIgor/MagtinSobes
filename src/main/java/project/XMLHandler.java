package project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*
Class that execute methods for working with xml tags
 */

public class XMLHandler {
    private Path firstXML = Paths.get("1.xml");
    private Path secondXML = Paths.get("2.xml");
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    List<String> xml = new ArrayList<>();

    // creating an xml file structure
    public void writeXml(List<String> list) {
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element entries = doc.createElement("entries");
            for (String element : list) {
                Element entry = doc.createElement("entry");
                Element field = doc.createElement("field");
                field.setTextContent(element);
                entries.appendChild(entry);
                entry.appendChild(field);
            }
            doc.appendChild(entries);
            writeDocument(doc, firstXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // writing the xml file structure
    private void writeDocument(Document document, Path fileName) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            tr.setOutputProperty(
                    OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(document);
            FileOutputStream fos = new FileOutputStream(fileName.toAbsolutePath().toString());
            StreamResult result = new StreamResult(fos);
            tr.transform(source, result);
            System.out.println("Создан файл по пути: " + fileName.toAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // XSLT conversion of the first xml file
    public void readXML() {
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(firstXML.toString());
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    xml.add(element.getTextContent());
                }
            }
            writeSecondXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // reading the second xml file
    public void readSecondXML() {
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("2.xml");
            NodeList nodes = doc.getChildNodes().item(0).getChildNodes();
            long result = 0;
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    result += Integer.parseInt(element.getAttribute("field"));
                }
            }
            System.out.println("Сумма: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // parser of the second xml file
    public void writeSecondXML() {
        Path path1 = Paths.get(firstXML.toAbsolutePath().toString());
        Path path2 = Paths.get(secondXML.toAbsolutePath().toString());
        InputStream inputXSL = getClass().getResourceAsStream("/template.xsl");
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            StreamSource xslStream = new StreamSource(inputXSL);
            Transformer transformer = factory.newTransformer(xslStream);
            StreamSource in = new StreamSource(path1.toFile());
            StreamResult out = new StreamResult(path2.toFile());
            transformer.transform(in, out);
            System.out.println("Создан файл по пути:" + path2.toAbsolutePath());
            readSecondXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

