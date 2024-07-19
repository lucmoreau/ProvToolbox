package org.openprovenance.prov.service;

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
    private final Map<String, Map<String, String>> baseTypes;

    public TemplatesToDot(List<TemplateQuery.TemplateConnection> templateConnections, Map<String, Map<String, String>> baseTypes, Map<String, Map<String, Map<String, String>>> ioMap, TemplateDispatcher templateDispatcher, ProvFactory pf) {
        super(pf);
        this.templateConnections = templateConnections;
        this.templateDispatcher = templateDispatcher;
        this.ioMap = ioMap;
        this.baseTypes = baseTypes;
    }

    public static String createHtmlTable(TemplateInfo templateInfo,
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
        html.append(String.format("    <TD ROWSPAN=\"3\" HREF=\"%s\"  TARGET=\"_blank\">%s </TD>\n", templateInfo.url, templateInfo.templateId));
        for (int i = 0; i < inputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\" HREF=\"%s\" TARGET=\"_blank\">%s</TD>\n",
                    inputsPorts.get(i), inputsColors.get(i), templateInfo.url.replace(".svg", "/"+inputsNames.get(i)), inputsNames.get(i)));
        }
        html.append("  </TR>\n");

        // Second row for outputs
        html.append("  <TR>\n");
        for (int i = 0; i < outputsNames.size(); i++) {
            html.append(String.format("    <TD PORT=\"%s\" BGCOLOR=\"%s\"  HREF=\"%s\"  TARGET=\"_blank\">%s</TD>\n",
                    outputsPorts.get(i), outputColors.get(i), templateInfo.url.replace(".svg", "/"+outputsNames.get(i)), outputsNames.get(i)));
        }
        html.append("  </TR>\n");

        // Close the table
        html.append("</TABLE>");

        return html.toString();
    }

   final Map<String, String> provcolors = new HashMap<>() {{
        put("http://www.w3.org/ns/prov#Entity", ENTITY_FILLCOLOUR);
        put("http://www.w3.org/ns/prov#Activity", ACTIVITY_FILL_COLOUR);
        put("http://www.w3.org/ns/prov#Agent", AGENT_FILLCOLOUR);
    }};




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
        Set<TemplateInfo> allTemplates = new HashSet<>();
        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            allTemplates.add(TemplateInfo.of(templateConnection.in_template, templateName(templateConnection.in_template, templateConnection.in_id),  url(templateConnection.in_template,  templateConnection.in_id)));
            allTemplates.add(TemplateInfo.of(templateConnection.out_template,templateName(templateConnection.out_template,templateConnection.out_id), url(templateConnection.out_template, templateConnection.out_id)));
        }

        Map<String, Map<String, String>> inputs=ioMap.get("input"); //templateDispatcher.getInputs();
        Map<String, Map<String, String>> outputs=ioMap.get("output"); //templateDispatcher.getOutputs();


        for (TemplateInfo templateInfo: allTemplates) {
           // System.out.println("- templateInfo: " + templateInfo);

            String template = templateInfo.template;
            String templateId = templateInfo.templateId;
            Map<String, String> templateBaseTypes = baseTypes.get(template);



            List<String> inputsNames  = new ArrayList<>(inputs.get(template).keySet());
            List<String> inputPorts   = inputsNames.stream().map(s -> portName(template,templateId,s)).collect(Collectors.toList());
            List<String> inputsColors = inputsNames.stream().map(s -> provcolors.get(templateBaseTypes.get(s))).collect(Collectors.toList()); //inputPorts.stream().map(s -> "lightgreen").collect(Collectors.toList());

            List<String> outputsNames  = new ArrayList<>(outputs.get(template).keySet());
            List<String> outputsPorts  = outputsNames.stream().map(s -> portName(template, templateId,s)).collect(Collectors.toList());
            List<String> outputsColors = outputsNames.stream().map(s -> provcolors.get(templateBaseTypes.get(s))).collect(Collectors.toList()); //outputsPorts.stream().map(s -> "orange").collect(Collectors.toList());


            String html = createHtmlTable(templateInfo, inputsNames, inputPorts, inputsColors, outputsNames, outputsPorts, outputsColors);
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

    String headstyle="invempty";
    String tailstyle="empty";

    private void emitEdge(String source, String destination, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append("\n");
        sb.append(source).append(":n"); // anchor to north
        sb.append(" -> ");
        sb.append(destination).append(":s"); // anchor to south
        sb.append("[dir=\"both\", arrowhead=\"").append(headstyle).append("\", arrowtail=\"").append(tailstyle).append("\"]");
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

    private String url(String template, Integer id) {
        return "/ptl/provapi/template/" + template+"/"+id + ".svg";
    }

    public static class TemplateInfo {
        private final String template;
        private final String templateId;
        private final String url;

        private TemplateInfo (String template, String templateId, String url) {
            this.template=template;
            this.templateId=templateId;
            this.url=url;
        }
        static public TemplateInfo of(String template, String templateId, String url) {
            return new TemplateInfo(template, templateId, url);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TemplateInfo that = (TemplateInfo) o;
            return Objects.equals(template, that.template) && Objects.equals(templateId, that.templateId) && Objects.equals(url, that.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(template, templateId, url);
        }
    }
}
