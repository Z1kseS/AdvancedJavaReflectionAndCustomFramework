package framework.core.reader.impl;

import java.util.List;

import framework.core.reader.BeanDefinitionReader;
import framework.parsers.Bean;
import framework.parsers.SaxParser;

public class SaxXmlBeanDefinitionReader implements BeanDefinitionReader<String> {

	public List<Bean> getBeanList(String fileName) {
		return new SaxParser(fileName).getBeanList();
	}
}
