package org.lsis.d21.demo_java_xml.sax;

import java.io.FileReader;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Parcours d'un document XML avec SAX. (pas d'écriture).
 * 
 * @author vincent
 *
 */
public class TestSAX {

	public static void main(String args[]) throws Exception {
		// Définition d'un reader.
		XMLReader xmlReader = XMLReaderFactory.createXMLReader();

		// Instanciation d'un handler ("Push Parsing" car on va réagir aux
		// événements).
		MyHandler xmlHandler = new TestSAX.MyHandler();
		xmlReader.setContentHandler(xmlHandler);
		xmlReader.setErrorHandler(xmlHandler);

		// Parsing d'un fichier XML.
		FileReader r = new FileReader(TestSAX.class.getResource("/books.xml").getPath());
		xmlReader.parse(new InputSource(r));

	}

	public static class MyHandler extends DefaultHandler {

		////////////////////////////////////////////////////////////////////
		// Event handlers.
		////////////////////////////////////////////////////////////////////

		@Override
		public void startDocument() {
			// Appelée lors de louverture du document.
			System.out.println("Start document");
		}

		@Override
		public void endDocument() {
			System.out.println("End document");
		}

		@Override
		public void startElement(String uri, String name, String qName, Attributes atts) {
			// Appelée lors de l'ouverture d'un élément.
			if ("".equals(uri))
				System.out.println("\tStart element: " + qName);
			else
				System.out.println("\tStart element: {" + uri + "}" + name);

			if ("Title".equals(name)) {
				System.out.println("TIIIIIIIITRE");
			}
		}

		@Override
		public void endElement(String uri, String name, String qName) {
			if ("".equals(uri))
				System.out.println("\tEnd element: " + qName);
			else
				System.out.println("\tEnd element:   {" + uri + "}" + name);
		}

		@Override
		public void characters(char ch[], int start, int length) {
			System.out.print("\tCharacters:    \"");
			for (int i = start; i < start + length; i++) {
				switch (ch[i]) {
				case '\\':
					System.out.print("\\\\");
					break;
				case '"':
					System.out.print("\\\"");
					break;
				case '\n':
					System.out.print("\\n");
					break;
				case '\r':
					System.out.print("\\r");
					break;
				case '\t':
					System.out.print("\\t");
					break;
				default:
					System.out.print(ch[i]);
					break;
				}
			}
			System.out.print("\"\n");
		}
	}
}
