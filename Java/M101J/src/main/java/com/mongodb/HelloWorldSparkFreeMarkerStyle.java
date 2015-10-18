package com.mongodb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by navargab on 17/10/2015.
 */
public class HelloWorldSparkFreeMarkerStyle {

    public static void main(String[] args) {
        final Configuration config = new Configuration();
        config.setClassForTemplateLoading(HelloWorldSparkFreeMarkerStyle.class, "/");
        Spark.get(new Route("/") {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                //Output of the template
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = config.getTemplate("hello.ftl");
                    //To map the values that will be used inside the templates with the maker => ${key}
                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "FreeMarker");
                    helloTemplate.process(helloMap, writer);
                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer;
            }
        });
    }
}
