module prov.service.core {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires io.swagger.v3.oas.annotations;
    requires jakarta.servlet;
    requires jakarta.ws.rs;
    requires org.apache.commons.io;
    requires org.apache.logging.log4j;
    requires prov.interop;
    requires prov.model;
    requires prov.storage.api;
    requires prov.log;
    requires prov.storage.filesystem;
    requires org.quartz;
    requires resteasy.multipart.provider;
}