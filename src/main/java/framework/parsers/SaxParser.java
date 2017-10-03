package framework.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxParser extends DefaultHandler implements Parser {

	private List<Bean> beanList;
	private List<Bean> interceptorList;

	private String xmlFileName;
	private Stack<Bean> beansTmp = new Stack<>();

	private Stack<Property> propertiesTmp = new Stack<>();
	private Stack<Bean> constructorsTmp = new Stack<>();

	private Stack<Boolean> isPropertyTime = new Stack<>();

	public List<Bean> getBeanList() {
		return beanList;
	}

	public List<Bean> getInterceptorList() {
		return interceptorList;
	}

	public SaxParser(String xmlFileName) {
		this.xmlFileName = xmlFileName;
		beanList = new ArrayList<Bean>();
		interceptorList = new ArrayList<Bean>();
		parseDocument();
	}

	private void parseDocument() {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser parser = factory.newSAXParser();
			parser.parse(xmlFileName, this);
		} catch (ParserConfigurationException e) {
			System.out.println("ParserConfig error");
		} catch (SAXException e) {
			System.out.println("SAXException : xml not well formed");
		} catch (IOException e) {
			System.out.println("IO error");
		}
	}

	public String toString() {
		String res = "";
		for (Bean tmpB : beanList) {
			res += tmpB.toString() + "; ";
		}

		return res;
	}

	@Override
	public void startElement(String uri, String localName, String elementName, Attributes attributes)
			throws SAXException {

		Bean beanTmp = (beansTmp.isEmpty()) ? null : beansTmp.peek();
		Property propertyTmp = (propertiesTmp.isEmpty()) ? null : propertiesTmp.peek();
		Bean constructorTmp = (constructorsTmp.isEmpty()) ? null : constructorsTmp.peek();
		Boolean isProperty = (isPropertyTime.isEmpty()) ? null : isPropertyTime.peek();

		if (elementName.equalsIgnoreCase("bean") || elementName.equalsIgnoreCase("interceptor")) {
			String beanId = attributes.getValue("id");
			Bean operationBean = null;

			if (beanTmp != null) {
				if (isProperty && propertyTmp != null)
					operationBean = propertyTmp.getBean();
				else if (!isProperty && constructorTmp != null)
					operationBean = constructorTmp;
				else
					throw new IllegalArgumentException(
							"Inner bean can be passed only under property or constructor-arg");
			} else if (beanId != null) {
				operationBean = findBean(beanId);
				if (operationBean == null) {
					operationBean = new Bean();
				}
			}

			// TODO validate bean uniqueness

			operationBean.setName(beanId);
			operationBean.setClassName(attributes.getValue("class"));
			operationBean.setValue(attributes.getValue("value"));
			operationBean.setPrototype(attributes.getValue("scope"));
			operationBean.setStub(false);

			beansTmp.push(operationBean);

			if (beanId != null && !beanList.contains(operationBean))
				beanList.add(operationBean);
		}

		if (elementName.equalsIgnoreCase("constructor-arg")) {
			String ref = attributes.getValue("ref");

			if (ref == null) {
				constructorTmp = new Bean();
				constructorTmp.setClassName(attributes.getValue("type"));
				constructorTmp.setValue(attributes.getValue("value"));
				constructorsTmp.push(constructorTmp);
				isPropertyTime.push(false);

				beanTmp.getConstructorArg().add(constructorTmp);
			} else {
				Bean refBean = getRefBean(ref);
				constructorsTmp.push(refBean);
				isPropertyTime.push(false);
				beanTmp.getConstructorArg().add(getRefBean(ref));
			}
		}

		if (elementName.equalsIgnoreCase("property")) {
			String ref = attributes.getValue("ref");

			if (ref == null) {
				propertyTmp = new Property();
				Bean propertyBean = new Bean();
				propertyBean.setClassName(attributes.getValue("type"));
				propertyBean.setValue(attributes.getValue("value"));
				propertyTmp.setName(attributes.getValue("name"));
				propertyTmp.setBean(propertyBean);
				propertiesTmp.push(propertyTmp);
				isPropertyTime.push(true);

				beanTmp.getProperties().add(propertyTmp);
			} else {
				propertyTmp = new Property();
				propertyTmp.setBean(getRefBean(ref));
				propertyTmp.setName(attributes.getValue("name"));
				propertiesTmp.push(propertyTmp);
				isPropertyTime.push(true);

				beanTmp.getProperties().add(propertyTmp);
			}
		}
	}

	private Bean getRefBean(String ref) {
		if (ref == null)
			throw new NullPointerException();

		Bean possibleBean = findBean(ref);

		if (possibleBean == null) {
			possibleBean = new Bean();
			possibleBean.setStub(true);
			possibleBean.setName(ref);
		}

		beanList.add(possibleBean);
		return possibleBean;
	}

	private Bean findBean(String id) {
		for (Bean bean : beanList)
			if (bean.getName().equals(id))
				return bean;
		return null;
	}

	@Override
	public void endElement(String s, String s1, String element) throws SAXException {
		if (ParserTypes.BEAN.propertyName().equals(element)) {
			beansTmp.pop();
		} else if (ParserTypes.INTERCEPTOR.propertyName().equals(element)) {
			beansTmp.pop();
		} else if (ParserTypes.PROPERTY.propertyName().equals(element)) {
			propertiesTmp.pop();
			isPropertyTime.pop();
		} else if (ParserTypes.CONSTRUCTOR.propertyName().equals(element)) {
			constructorsTmp.pop();
			isPropertyTime.pop();
		}
	}

	@Override
	public void characters(char[] ac, int start, int length) throws SAXException {
		// valueTmp = new String(ac, start, length);
		// TODO inner value?
	}
}
