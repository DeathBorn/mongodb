package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by navargab on 17/10/2015.
 */
public class HelloWorldFreeMarkerStyle {
    public static void main(String[] args) {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class, "/");

        try {
            Template helloTemplate = config.getTemplate("hello.ftl");
            //Output of the template
            StringWriter writer = new StringWriter();
            //To map the values that will be used inside the templates with the maker => ${key}
            Map<String, Object> helloMap = new HashMap<String, Object>();
            helloMap.put("name", "FreeMarker");
            helloTemplate.process(helloMap, writer);
            System.out.println(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
