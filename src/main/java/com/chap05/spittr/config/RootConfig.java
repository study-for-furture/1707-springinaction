package com.chap05.spittr.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by mitmo on 2017-09-03.
 */
@Configuration
@Import(DataConfig.class)
@ComponentScan(basePackages = {"com.chap05.spittr"},
    excludeFilters = {
            @ComponentScan.Filter(type= FilterType.ANNOTATION, value= EnableWebMvc.class)
    })
public class RootConfig {
}
