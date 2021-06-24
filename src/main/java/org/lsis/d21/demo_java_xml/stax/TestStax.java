package org.lsis.d21.demo_java_xml.stax;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class TestStax {

	public static void main(String[] args) throws XMLStreamException, IOException {

		URL booksUrl = TestStax.class.getResource("/books.xml");
		InputStream in = booksUrl.openStream();

		// 1. Création d'un XMLStreamReader via une fabrique pour parcourir le
		// document.
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLStreamReader parser = factory.createXMLStreamReader(in);

		// 2. Parcours explicite (~SAX sauf que c'est le programmeur qui
		// contrôle le parcours).

		// Dans l'exemple suivant, on affiche le contenu des nœuds Item.
		boolean inItem = false;
		for (int event = parser.next(); event != XMLStreamConstants.END_DOCUMENT; event = parser.next()) {
			switch (event) {
			case XMLStreamConstants.START_ELEMENT:
				if (parser.getLocalName().equals("Item")) {
					inItem = true;
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				if (parser.getLocalName().equals("Item")) {
					inItem = false;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				if (inItem == true) {
					System.out.println(parser.getText());
				}
				break;
			case XMLStreamConstants.CDATA:
				if (inItem) {
				}
				break;
			}
		}
		parser.close();
	}
}
