package com.madhubasavanna.wikipediadatalibrary.jsonimageurlclasses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.util.Map;

public class DataNamingStrategy extends PropertyNamingStrategy {
    private String replaceMap;

    public DataNamingStrategy(String replaceMap) {
        this.replaceMap = replaceMap;
    }


    // use this to change field name
    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field,
                               String defaultName) {
            if(defaultName.equals("imageDetails")){
            return replaceMap;
        }else {
            return super.nameForField(config, field, defaultName);
        }
    }

    // use this to change setter method field name
    @Override
    public String nameForSetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method, String defaultName) {
        if(defaultName.equals("imageDetails")){
            return replaceMap;
        }else {
            return super.nameForSetterMethod(config, method, defaultName);
        }
    }

    // use this to change getter method field name
    // should be same as nameForSetterMethod
    @Override
    public String nameForGetterMethod(MapperConfig<?> config,
                                      AnnotatedMethod method, String defaultName) {
        if(defaultName.equals("imageDetails")){
            return replaceMap;
        }else {
            return super.nameForGetterMethod(config, method, defaultName);
        }
    }
}
