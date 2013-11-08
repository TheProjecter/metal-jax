/**
 * @copyright Jay Tang 2012. All rights reserved.
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package metal.core.mop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

public class ClassFinder extends ClassPathScanningCandidateComponentProvider {

	private String[] basePackages = new String[0];

	public ClassFinder() {
		super(false);
	}

	public void setBasePackage(String basePackage) {
		this.basePackages = StringUtils.tokenizeToStringArray(basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
	}

	public void setIncludeFilters(TypeFilter... filters) {
		for (TypeFilter filter : filters) {
			addIncludeFilter(filter);
		}
	}

	public void setExcludeFilters(TypeFilter... filters) {
		for (TypeFilter filter : filters) {
			addExcludeFilter(filter);
		}
	}

	public final List<Class<?>> findClasses() {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition candidate : candidates) {
				classes.add(ClassUtils.resolveClassName(candidate.getBeanClassName(), ClassUtils.getDefaultClassLoader()));
			}
		}
		return classes;
	}

}
