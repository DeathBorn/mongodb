package com.mongodb;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by navargab on 17/10/2015.
 */
public class HelloWorldSparkStyle {
    public static void main(String[] args) {
        Spark.get(new Route("/") {
            @Override
            public Object handle(final Request request,
                                 final Response response){
                return "Hello World from Spark";
            }
        });
    }
}
