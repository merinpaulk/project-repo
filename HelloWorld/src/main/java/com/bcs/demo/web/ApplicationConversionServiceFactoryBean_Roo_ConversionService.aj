// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.bcs.demo.web;

import com.bcs.demo.domain.Patent;
import com.bcs.demo.web.ApplicationConversionServiceFactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;

privileged aspect ApplicationConversionServiceFactoryBean_Roo_ConversionService {
    
    declare @type: ApplicationConversionServiceFactoryBean: @Configurable;
    
    public Converter<Patent, String> ApplicationConversionServiceFactoryBean.getPatentToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.bcs.demo.domain.Patent, java.lang.String>() {
            public String convert(Patent patent) {
                return new StringBuilder().append(patent.getPatentNumber()).toString();
            }
        };
    }
    
    public Converter<Long, Patent> ApplicationConversionServiceFactoryBean.getIdToPatentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.bcs.demo.domain.Patent>() {
            public com.bcs.demo.domain.Patent convert(java.lang.Long id) {
                return Patent.findPatent(id);
            }
        };
    }
    
    public Converter<String, Patent> ApplicationConversionServiceFactoryBean.getStringToPatentConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.bcs.demo.domain.Patent>() {
            public com.bcs.demo.domain.Patent convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Patent.class);
            }
        };
    }
    
    public void ApplicationConversionServiceFactoryBean.installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getPatentToStringConverter());
        registry.addConverter(getIdToPatentConverter());
        registry.addConverter(getStringToPatentConverter());
    }
    
    public void ApplicationConversionServiceFactoryBean.afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
    
}
