package com.graphql.study.demo.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;
import com.graphql.study.demo.converter.GsonHttpMessageConverter;
import com.graphql.study.demo.util.CoreUtil;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    protected static Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        if (logger.isDebugEnabled()) {
            List<String> convertList = converters.stream()
                .map(convert -> convert.getClass().getName())
                .collect(Collectors.toList());

            logger.debug(">>> configureMessageConverters converters: {}", CoreUtil.toJson(convertList));
        }

        converters.clear();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        converters.add(gsonHttpMessageConverter);
    }

    @Bean
    public ViewResolver viewResolver() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>> viewResolver");
        }

        HandlebarsViewResolver handlebarsViewResolver = new HandlebarsViewResolver();
        handlebarsViewResolver.setPrefix("classpath:/templates");
        handlebarsViewResolver.setSuffix(".hbs");
        handlebarsViewResolver.setCache(false);
        return handlebarsViewResolver;
    }
}
