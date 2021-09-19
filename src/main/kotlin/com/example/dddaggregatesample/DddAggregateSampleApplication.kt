package com.example.dddaggregatesample

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.AnnotationBeanNameGenerator
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(nameGenerator = FQCNBeanNameGenerator::class)
class DddAggregateSampleApplication

fun main(args: Array<String>) {
	runApplication<DddAggregateSampleApplication>(*args)
}

class FQCNBeanNameGenerator : AnnotationBeanNameGenerator() {
	override fun buildDefaultBeanName(definition: BeanDefinition): String {
		return definition.beanClassName ?: ""
	}
}
