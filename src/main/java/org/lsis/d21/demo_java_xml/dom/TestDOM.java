package org.lsis.d21.demo_java_xml.dom;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class TestDOM {

	public static void lecture() throws SAXException, IOException, ParserConfigurationException {
		File xmlDocumentFile = new File(TestDOM.class.getResource("/books.xml").getFile());
		// 1. Création d'une instance de DocumentBuilder.
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		// 2. Chargement du document en mémoire depuis un fichier.
		// Remarque : on peut utiliser n'importe quelle URL.
		Document document = documentBuilder.parse(xmlDocumentFile);

		// 3. Manipulation du document.

		// Affichage du nombre de nœuds Item
		System.out.println("Nombre d'items: " + document.getElementsByTagName("Item").getLength());

		// Affichage de la valeur textuelle des nœuds Item
		NodeList nodeList = document.getElementsByTagName("Item");
		for (int i = 0; i < nodeList.getLength(); i++) {
			System.out.println(nodeList.item(i).getTextContent());
		}
	}

	public static void ecriture() throws ParserConfigurationException, IOException {
		// 1. Création d'une instance de DocumentBuilder.
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		// 2. Création d'un document.
		Document doc = documentBuilder.newDocument();

		// 3. Écriture du document.
		// Création de l'élément racine Books
		Element booksElement = doc.createElement("Books");
		doc.appendChild(booksElement);

		// Création de 2 livres.
		Element book1Element = doc.createElement("Book");
		Element title1Element = doc.createElement("title");
		title1Element.setTextContent("Titre du livre 1");
		book1Element.appendChild(title1Element);

		Element year1Element = doc.createElement("annee");
		year1Element.setTextContent("2015");
		book1Element.appendChild(year1Element);
		booksElement.appendChild(book1Element);

		Element book2Element = doc.createElement("Book");
		Element title2Element = doc.createElement("title");
		title2Element.setTextContent("Titre du livre 2");
		book2Element.appendChild(title2Element);
		Element year2Element = doc.createElement("annee");
		year2Element.setTextContent("2016");
		book2Element.appendChild(year2Element);
		booksElement.appendChild(book2Element);

		// 4. Serialisation du document dans un fichier.
		OutputFormat outputFormat = new OutputFormat(doc);

		XMLSerializer xmlSerializer = new XMLSerializer(new StringWriter(), outputFormat);
		xmlSerializer.asDOMSerializer();
		xmlSerializer.setOutputByteStream(new BufferedOutputStream(new FileOutputStream("/tmp/d21.xml")));
		xmlSerializer.serialize(doc.getDocumentElement());
	}

	public static void main(String args[]) throws Exception {
		lecture();
		ecriture();
	}

}
