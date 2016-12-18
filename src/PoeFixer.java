import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PoeFixer {
	private String encoding = "UTF-8";
	private List<String> extraResourcesInStringArray = new ArrayList<String>();

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void addExtraResourceString(String resourceInString) {
		extraResourcesInStringArray.add(resourceInString);
	}

	public void clearExtraResourceStringArray() {
		extraResourcesInStringArray.clear();
	}

	public void fix(File srcFile, File destFile) throws Exception {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(srcFile);
		// Fix Strings
		NodeList nodes = doc.getElementsByTagName("string");
		if (nodes != null && nodes.getLength() > 0) {
			for (int i = 0; i < nodes.getLength(); i++) {
				Node item = nodes.item(i);
				// Fix Key
				if (item.getAttributes() != null) {
					Node keyNode = item.getAttributes().getNamedItem("name");
					if (keyNode != null) {
						String key = keyNode.getTextContent();
						((Element) item).setAttribute("name", fixKey(key));
					}
				}
				// Fix Value
				if (item.getFirstChild() != null) {
					String value = item.getFirstChild().getNodeValue();
					((Element) item).getFirstChild().setNodeValue(fixValue(value));
				}
			}
		}
		// add new resources
		for (String childNodeString : extraResourcesInStringArray) {
			addStringChildNode(docBuilder, doc, childNodeString, encoding);
		}
		printXmlDocument(doc, destFile);
	}

	// -------------------->

	private static void addStringChildNode(DocumentBuilder docBuilder, Document doc, String childNodeString,
			String encoding) throws SAXException, IOException {
		Document extraResourcesDoc = docBuilder.parse(new ByteArrayInputStream(childNodeString.getBytes(encoding)));
		Node extraResourcesNode = doc.importNode(extraResourcesDoc.getDocumentElement(), true);
		doc.getDocumentElement().appendChild(extraResourcesNode);
	}

	private static void printXmlDocument(Document document, File destPath)
			throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(destPath);
		Source input = new DOMSource(document);
		transformer.transform(input, output);
	}

	// -------------------->

	private static String fixKey(String key) {
		return key;
	}

	private static String fixValue(String value) {
		if (!isStringEmpty(value)) {
			value = value.trim();
			value = value.indexOf('\"') == 0 ? value.replaceFirst("\"", "") : value;
			value = value.lastIndexOf('\"') == value.length() - 1 ? value.substring(0, value.length() - 1) : value;
			value = value.replaceAll("'", "\\\\'");
			value = value.replaceAll("‘", "\\\\‘");
			int count = 1;
			while (value.contains("%s")) {
				value = value.replaceFirst("\\%s", "\\%" + count + "\\$s");
				count++;
			}
		}
		return value;
	}

	// -------------------->

	private static boolean isStringEmpty(String string) {
		return string == null || string.trim().isEmpty();
	}
}
