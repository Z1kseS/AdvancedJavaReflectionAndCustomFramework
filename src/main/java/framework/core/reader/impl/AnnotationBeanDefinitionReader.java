package framework.core.reader.impl;

import java.util.List;

import framework.core.reader.BeanDefinitionReader;
import framework.parsers.Bean;
import framework.parsers.PackageParser;

public class AnnotationBeanDefinitionReader implements BeanDefinitionReader<String> {

	public List<Bean> getBeanList(String packageName) {
		return new PackageParser(packageName).getBeanList();
	}
}
