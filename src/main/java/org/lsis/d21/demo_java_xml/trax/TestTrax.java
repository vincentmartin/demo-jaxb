package org.lsis.d21.demo_java_xml.trax;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Transformation avec XSLT.
 * 
 * @author vincent
 *
 */
public class TestTrax {

	static public void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, TransformerException {

		// 1. On charge le document avec DOM.
		File xmlDocumentFile = new File(TestTrax.class.getResource("/tables.xml").getFile());
		
		// Parsing du document XML avec DOM.
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = documentBuilder.parse(xmlDocumentFile);

		// //////////////////////////////////////////////////////////////////

		// 2. Instanciation d'un TransformerFactory.
		TransformerFactory factory = TransformerFactory.newInstance();

		// 3. Instanciation d'un Transformer et spécification d'un fichier XSL
		// pour la transformation.
		Transformer transformer = factory.newTransformer(new StreamSource(
				new FileInputStream(new File(TestTrax.class.getResource("/encheres_objets.xsl").getPath()))));

		// 4. Définition de la source et de la destination.
		Source XMLinput = new DOMSource(document);
		Result XMLoutput = new StreamResult(new FileOutputStream("/tmp/tables.html"));

		transformer.transform(XMLinput, XMLoutput);
	}
}
