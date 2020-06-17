open module degubi.web {
    requires java.compiler;
    requires java.instrument;
    requires java.sql;
    requires java.json.bind;
    
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.data.commons;
    requires spring.data.mongodb;
    requires spring.web;
    
    requires mongo.java.driver;
}