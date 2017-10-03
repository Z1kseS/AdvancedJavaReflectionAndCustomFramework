package framework.core.reader;

import java.util.List;

import framework.parsers.Bean;

public interface BeanDefinitionReader<T> {
	List<Bean> getBeanList(T provider);
}
