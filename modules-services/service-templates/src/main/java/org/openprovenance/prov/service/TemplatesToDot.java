package org.openprovenance.prov.service;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;



public class TemplatesToDot extends ProvToDot {

    private static final Logger logger = LogManager.getLogger(TemplatesToDot.class);
    private final List<TemplateQuery.TemplateConnection> templateConnections;
    private final TemplateDispatcher templateDispatcher;
    private final Map<String, Map<String, Map<String, String>>> ioMap;

    public TemplatesToDot(List<TemplateQuery.TemplateConnection> templateConnections, Map<String, Map<String, Map<String, String>>> ioMap, TemplateDispatcher templateDispatcher, ProvFactory pf) {
        super(pf);
        this.templateConnections = templateConnections;
        this.templateDispatcher = templateDispatcher;
        this.ioMap = ioMap;
    }

    public static String createHtmlTable(String template,
                                         String templateId,
                                         List<String> inputsNames,
                                         List<String> inputsPorts,
                                         List<String> inputsColors,
                                         List<String> outputsNames,
                                         List<String> outputsPorts,
                                         List<String> outputColors) {
        StringBuilder html = new StringBuilder();

        // Start building the HTML for the table
        html.append("<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n");

        // First row with rowspan and input cells
        html.append("  <TR>\n");
        html.append("    <TD ROWSPAN=\"3\">" +  templateId + "</TD>\n"); // Static cell with rowspan
        for (int i = 0; i < inputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\" >%s</TD>\n",
                    inputsPorts.get(i), inputsColors.get(i), inputsNames.get(i)));
        }
        html.append("  </TR>\n");

        // Second row for outputs
        html.append("  <TR>\n");
        for (int i = 0; i < outputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\" >%s</TD>\n",
                    outputsPorts.get(i), outputColors.get(i), outputsNames.get(i)));
        }
        html.append("  </TR>\n");

        // Close the table
        html.append("</TABLE>");

        return html.toString();
    }


    public void convert(Document graph, OutputStream os, String title) {

        try {
            File dotFile=File.createTempFile("temp", ".dot");
            logger.info("dotFile: " + dotFile);
            convert(graph, new PrintStream(new FileOutputStream(dotFile)) ,title);
            Runtime runtime = Runtime.getRuntime();
            java.lang.Process proc = runtime.exec("dot  -Tsvg " + dotFile);
            InputStream is=proc.getInputStream();
            org.apache.commons.io.IOUtils.copy(is, os);
            logger.info("finished conversion to svg");
            @SuppressWarnings("unused")
            boolean resultCode=dotFile.delete();

        } catch (IOException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }



    }

    public void convert(Document doc, PrintStream out, String title) {
        if (title!=null) name=title;
        prelude(doc, out);

        // pairs <template, templateInstance>
        Set<Pair<String,String>> allTemplates = new HashSet<>();
        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            allTemplates.add(Pair.of(templateConnection.in_template, templateName(templateConnection.in_template, templateConnection.in_id)));
            allTemplates.add(Pair.of(templateConnection.out_template,templateName(templateConnection.out_template,templateConnection.out_id)));
        }

        Map<String, Map<String, String>> inputs=ioMap.get("input"); //templateDispatcher.getInputs();
        Map<String, Map<String, String>> outputs=ioMap.get("output"); //templateDispatcher.getOutputs();

        //System.out.println("inputs: " + inputs);
        //System.out.println("outputs: " + outputs);
        //System.out.println("allTemplates: " + allTemplates);


        for (Pair<String, String> template_templateInstance: allTemplates) {
           // System.out.println("- template_templateInstance: " + template_templateInstance);

            String template = template_templateInstance.getLeft();
            String templateId = template_templateInstance.getRight();

            List<String> inputsNames  = new ArrayList<>(inputs.get(template).keySet());
            List<String> inputPorts   = inputsNames.stream().map(s -> portName(template,templateId,s)).collect(Collectors.toList());
            List<String> inputsColors = inputPorts.stream().map(s -> "lightgreen").collect(Collectors.toList());

            List<String> outputsNames  = new ArrayList<>(outputs.get(template).keySet());
            List<String> outputsPorts  = outputsNames.stream().map(s -> portName(template, templateId,s)).collect(Collectors.toList());
            List<String> outputsColors = outputsPorts.stream().map(s -> "orange").collect(Collectors.toList());


            String html = createHtmlTable(template, templateId, inputsNames, inputPorts, inputsColors, outputsNames, outputsPorts, outputsColors);
            emitTemplate(template, templateId, html, out);

        }

        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            emitEdge(qualifiedPortName(templateConnection.in_template,  templateName(templateConnection.in_template, templateConnection.in_id),  templateConnection.in_property),
                     qualifiedPortName(templateConnection.out_template, templateName(templateConnection.out_template,templateConnection.out_id), templateConnection.out_property),
                     out);
        }

        postlude(doc,out);
        out.close();

    }


    private void emitEdge(String source, String destination, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append("\n");
        sb.append(source);
        sb.append(" -> ");
        sb.append(destination);
        sb.append(";\n");
        out.println(sb.toString());
    }

    private String portName(String template, String templateId, String property) {
        return  template+"_"+templateId+"_"+property;
    }


    private String qualifiedPortName(String template, String templateId, String property) {
        return templateId + ":" + portName(template, templateId, property);
    }

    public void emitTemplate(String template, String templateId, String htmlTable, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append("\n");
        sb.append("node [shape=plaintext]\n");
        sb.append(templateId);
        sb.append(" [label=<");
        sb.append(htmlTable);
        sb.append(">];\n");
        out.println(sb.toString());
    }

    private String templateName(String template, Integer id) {
        return template+"_"+id;
    }

}
