package com.dcits.cloud.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com")
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/")
				.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver CommonsMultipartResolver() {
		//
		CommonsMultipartResolver multipart = new CommonsMultipartResolver();
		multipart.setMaxUploadSize(1000000000);
		multipart.setDefaultEncoding("utf-8");
		//
		return multipart;
	}
	@Bean
	public ContentNegotiatingViewResolver ContentNegotiatingViewResolver() {
		List<ViewResolver> viewResolverList = new ArrayList<ViewResolver>();
		List<View> deaultViewList = new ArrayList<View>();
		ContentNegotiatingViewResolver contentView = new ContentNegotiatingViewResolver();
		//
		InternalResourceViewResolver internalView = new InternalResourceViewResolver();
		internalView.setPrefix("/");
		internalView.setSuffix(".jsp");
		//
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		viewResolverList.add(internalView);

		deaultViewList.add(jsonView);
		contentView.setViewResolvers(viewResolverList);
		contentView.setDefaultViews(deaultViewList);
		return contentView;
	}

}
