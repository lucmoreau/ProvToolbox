package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.service.core.progress.ProgressListener;
import org.openprovenance.prov.service.core.progress.VizStages;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.openprovenance.prov.model.NamespacePrefixMapper.DOT_NS;


public class TemplatesToDot extends ProvToDot {

    private static final Logger logger = LogManager.getLogger(TemplatesToDot.class);
    private final List<TemplateQuery.TemplateConnection> templateConnections;
    private final CatalogueDispatcherInterface<FileBuilder> templateDispatcher;
    private final Map<String, Map<String, Map<String, String>>> ioMap;
    private final Map<String, Map<String, String>> baseTypes;
    private final ProvFactory pf;
    private final String style;
    private final TemplateQuery templateQuery;
    private final String principal;
    private final String provAPI;
    private final Map<String, String> parameters;
    private final Map<String, Map<String, List<String>>> selectedSuccessors;
    private final boolean withIcons;
    private final String iconDirectory;

    public TemplatesToDot(List<TemplateQuery.TemplateConnection> templateConnections,
                          String style,
                          boolean withIcons,
                          String iconDirectory,
                          Map<String, String> parameters,
                          Map<String, Map<String, String>> baseTypes,
                          Map<String, Map<String, Map<String, String>>> ioMap,
                          CatalogueDispatcherInterface<FileBuilder> templateDispatcher,
                          Map<String, Map<String, List<String>>> selectedSuccessors,
                          ProvFactory pf,
                          TemplateQuery templateQuery,
                          String principal,
                          String provAPI) {
        super(pf);
        this.pf=pf;
        this.templateConnections = templateConnections;
        this.templateDispatcher = templateDispatcher;
        this.ioMap = ioMap;
        this.baseTypes = baseTypes;
        this.style=style;
        this.templateQuery=templateQuery;
        this.principal=principal;
        this.provAPI=provAPI;
        this.parameters=parameters;
        this.selectedSuccessors=selectedSuccessors;
        this.withIcons=withIcons;
        this.iconDirectory=iconDirectory;
    }



    public static String createHtmlTable(TemplateInfo templateInfo,
                                         boolean withIcons,
                                         String iconDirectory,
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

        if (withIcons) {
            // IMPORTANT NOTE
            // While graphviz documentation indicates that svg images <IMG> are permitted, in practice it does not seem to be the case.
            // Thus, we refer to png file.
            // Such image files MUST be on the file system.
            // When generating an SVG, graphviz does not embed the images, but instead links to them.
            // This is problematic, when the visualisation is served by a service to a browser, because the browser will not be able to dereference a file image.
            // For this to work, the client needs to rewrite the url.
            // This is implemented by function rewriteImageHrefs(svgElement) in form.html
            String iconImage="<IMG SRC=\"" + iconDirectory + "/" + templateInfo.template + ".png\"/>";
            html.append(String.format("    <TD ROWSPAN=\"3\" HREF=\"%s\"  TARGET=\"_blank\">%s</TD>\n", templateInfo.url, iconImage));
        }

        String typeInfo="";
        if (templateInfo.semanticType!=null &&  !templateInfo.semanticType.equals("None1")) {
            typeInfo+="<BR/>(";
            typeInfo+=templateInfo.semanticType;
            typeInfo+=")";
        }

        html.append(String.format("    <TD ROWSPAN=\"3\" HREF=\"%s\"  TARGET=\"_blank\">%s %s</TD>\n", templateInfo.url, templateInfo.templateId, typeInfo));

        if (inputsNames.isEmpty()) {
            html.append("    <TD></TD>\n");
        }
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

       // System.out.println(html.toString());

        return html.toString();
    }

    final Map<String, String> provcolors = new HashMap<>() {{
        put("http://www.w3.org/ns/prov#Entity", ENTITY_FILLCOLOUR);
        put("http://www.w3.org/ns/prov#Activity", ACTIVITY_FILL_COLOUR);
        put("http://www.w3.org/ns/prov#Agent", AGENT_FILLCOLOUR);
    }};




    public void convert(Document graph, OutputStream os, String title, ProgressListener listener) {
        File dotFile;

        listener.started(VizStages.PROV_BUILD);
        long provStart = System.nanoTime();
        try {
            dotFile = File.createTempFile("temp", ".dot");
            logger.debug("dotFile: " + dotFile);
            convert(graph, new PrintStream(new FileOutputStream(dotFile)), title);
            listener.done(VizStages.PROV_BUILD, (System.nanoTime() - provStart) / 1_000_000);
        } catch (IOException e) {
            listener.failed(VizStages.PROV_BUILD, e);
            logger.throwing(e);
            throw new UncheckedException(e);
        } catch (RuntimeException e) {
            listener.failed(VizStages.PROV_BUILD, e);
            throw e;
        }
        listener.detail(VizStages.PROV_BUILD, "DOT " + dotFile.length() + " bytes");

        listener.started(VizStages.RENDER);
        long renderStart = System.nanoTime();
        long svgBytes;
        try {
            Runtime runtime = Runtime.getRuntime();
            java.lang.Process proc = runtime.exec("dot  -Tsvg " + dotFile);
            InputStream is = proc.getInputStream();
            svgBytes = org.apache.commons.io.IOUtils.copyLarge(is, os);
            listener.done(VizStages.RENDER, (System.nanoTime() - renderStart) / 1_000_000);
        } catch (IOException e) {
            listener.failed(VizStages.RENDER, e);
            logger.throwing(e);
            throw new UncheckedException(e);
        } catch (RuntimeException e) {
            listener.failed(VizStages.RENDER, e);
            throw e;
        }
        listener.detail(VizStages.RENDER, "SVG " + svgBytes + " bytes");

        logger.info("finished conversion to svg");
        @SuppressWarnings("unused")
        boolean resultCode = dotFile.delete();
    }

    public void convert(Document ignore, PrintStream out, String title) {
        switch (style) {
            case "template":
                convert_template(ignore, out, title);
                break;
            case "prov":
                convert_prov(ignore, out, title);
                break;
            case "entities":
                convert_entities(ignore, out, title);
                break;
            default:
                throw new UnsupportedOperationException("style not supported: " + style);
        }
    }

    public void convert_prov(Document ignore, PrintStream out, String title) {
        Set<TemplateQuery.RecordEntry> the_templates = new HashSet<>();

        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            TemplateQuery.RecordEntry entry_in=new TemplateQuery.RecordEntry();
            entry_in.table=templateConnection.in_template;
            entry_in.key=templateConnection.in_id;
            the_templates.add(entry_in);

            TemplateQuery.RecordEntry entry_out=new TemplateQuery.RecordEntry();
            entry_out.table=templateConnection.out_template;
            entry_out.key=templateConnection.out_id;
            the_templates.add(entry_out);
        }

        List<Object[]> the_records = new LinkedList<>();
        for (TemplateQuery.RecordEntry linked_record : the_templates) {
            Integer simple = linked_record.key;
            List<Object[]> simple_records = templateQuery.querySimple(linked_record.table, simple, false, principal);
            the_records.addAll(simple_records);
        }

        Document result=templateQuery.constructDocument(the_records);

        super.convert(result, out, title);

    }

    public void convert_entities(Document ignore, PrintStream out, String title) {
        // creates a map from in to out
        Map<QualifiedName,QualifiedName> map=new HashMap<>();
        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            QualifiedName outQn = qualifiedPortNameAsQn(templateConnection.out_template, String.valueOf(templateConnection.out_id), templateConnection.out_property);
            QualifiedName inQn = qualifiedPortNameAsQn(templateConnection.in_template, String.valueOf(templateConnection.in_id), templateConnection.in_property);
            map.put(inQn,outQn);
        }

        Document doc = pf.newDocument();

        Set<QualifiedName> seen=new HashSet<>();
        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {

            String template = templateConnection.in_template;
            String templateId = String.valueOf(templateConnection.in_id);
            String property = templateConnection.in_property;
            List<String> next=selectedSuccessors.get(template).get(property);
            if (next!=null) {
                for (String n: next) {
                    QualifiedName older = map.get(qualifiedPortNameAsQn(template, templateId, property));
                    QualifiedName newer = qualifiedPortNameAsQn(template, templateId, n);

                    if (!seen.contains(older)) {
                        seen.add(older);
                        doc.getStatementOrBundle().add(pf.newEntity(older));
                    }
                    if (!seen.contains(newer)) {
                        seen.add(newer);
                        doc.getStatementOrBundle().add(pf.newEntity(newer));
                    }

                    List<Attribute> attrs=new LinkedList<>();
                    attrs.add(pf.newAttribute(pf.newQualifiedName(DOT_NS,"style","dot"), "dashed", pf.getName().XSD_STRING));
                    WasDerivedFrom edge = pf.newWasDerivedFrom(null,newer, older, null, null, null, attrs);
                    doc.getStatementOrBundle().add(edge);
                }
            }

        }

        super.convert(doc, out, title);
    }


    public void convert_template(Document doc, PrintStream out, String title) {
        if (title!=null) name=title;
        prelude(doc, out);

        // pairs <template, templateInstance>
        Set<TemplateInfo> allTemplates = new HashSet<>();
        Map<String, String> shortNames = templateQuery.getShortNames();
        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            allTemplates.add(TemplateInfo.of(templateConnection.in_template, templateName(shortNames.get(templateConnection.in_template), templateConnection.in_id),  url(templateConnection.in_template,  templateConnection.in_id), templateConnection.in_type));
            allTemplates.add(TemplateInfo.of(templateConnection.out_template,templateName(shortNames.get(templateConnection.out_template),templateConnection.out_id), url(templateConnection.out_template, templateConnection.out_id), templateConnection.out_type));
        }

        Map<String, Map<String, String>> inputs=ioMap.get("input"); //templateDispatcher.getInputs();
        Map<String, Map<String, String>> outputs=ioMap.get("output"); //templateDispatcher.getOutputs();
        Set<String> overlayTemplates=new HashSet<>();
        // ── Virtual outputs for all-input (decorator/overlay) templates ──
        // Templates where ALL properties are inputs are stripped from the
        // output map by removeIf() in getIoMap(), so they never appear in
        // output_table and the loop below can never "arrive at" them.
        // Fix: for any template present in input_table (it references an
        // entity from this table) but absent from output_table (no declared
        // outputs), inject it into output_table using its input properties
        // as virtual output keys.  The traversal can then hop through the
        // template to follow predecessor_table derivation edges onward.
        for (String template : inputs.keySet()) {
            if (!outputs.containsKey(template)) {
                outputs.put(template, inputs.get(template));
                overlayTemplates.add(template);
            }
        }
        // ─────────────────────────────────────────────────────────────────


        for (TemplateInfo templateInfo: allTemplates) {
            //System.out.println("- templateInfo: " + templateInfo);

            String templateFullyQualifiedName = templateInfo.template;
            String templateId = templateInfo.templateId;
            Map<String, String> templateBaseTypes = baseTypes.get(templateFullyQualifiedName);


            String template = shortNames.get(templateFullyQualifiedName);

            //System.out.println("- template: " + template + " id: " + templateId);
            //System.out.println("  baseTypes: " + templateBaseTypes);
            //System.out.println("  inputs: " + inputs);
            //System.out.println("  outputs: " + outputs);

            Map<String, String> templateInputs = inputs.get(template);
            List<String> inputsNames  = (templateInputs==null)? List.of() : new ArrayList<>(templateInputs.keySet());
            List<String> inputPorts   = inputsNames.stream().map(s -> portName(template,templateId,s)).collect(Collectors.toList());
            List<String> inputsColors = inputsNames.stream().map(s -> provcolors.get(templateBaseTypes.get(s))).collect(Collectors.toList()); //inputPorts.stream().map(s -> "lightgreen").collect(Collectors.toList());

            Map<String, String> templateOutputs = outputs.get(template);
            List<String> outputsNames  = new ArrayList<>(templateOutputs.keySet());
            List<String> outputsPorts  = outputsNames.stream().map(s -> portName(template, templateId,s)).collect(Collectors.toList());
            List<String> outputsColors = outputsNames.stream().map(s -> provcolors.get(templateBaseTypes.get(s))).collect(Collectors.toList()); //outputsPorts.stream().map(s -> "orange").collect(Collectors.toList());


            String html = createHtmlTable(templateInfo, withIcons, iconDirectory, inputsNames, inputPorts, inputsColors, outputsNames, outputsPorts, outputsColors);
            if (overlayTemplates.contains(template)) {
                System.out.println(html);
            }
            emitTemplate(template, templateId, html, out);

        }

        for (TemplateQuery.TemplateConnection templateConnection : templateConnections) {
            emitEdge(qualifiedPortName(shortNames.get(templateConnection.in_template),  templateName(shortNames.get(templateConnection.in_template), templateConnection.in_id),  templateConnection.in_property),
                    qualifiedPortName(shortNames.get(templateConnection.out_template), templateName(shortNames.get(templateConnection.out_template),templateConnection.out_id), templateConnection.out_property),
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
    private QualifiedName qualifiedPortNameAsQn(String template, String templateId, String property) {
        return pf.newQualifiedName( provAPI + "/template/", template + "/"+ templateId + "/" + property, "ex");
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

    /*
    private String livePrefix(String relation) {
        return "/book/provapi/live/" + relation+"/" ;
    }

    private String urlPrefix(String template) {
        return "/book/provapi/template/" + template+"/";
    }

     */
    private String url(String template, Integer id) {
        return provAPI + "/template/" + template+"/"+id + ".svg";
    }

    public static class TemplateInfo {
        private final String template;
        private final String templateId;
        private final String url;
        private final String semanticType;

        private TemplateInfo (String template, String templateId, String url, String semanticType) {
            this.template=template;
            this.templateId=templateId;
            this.url=url;
            this.semanticType = semanticType;
        }
        static public TemplateInfo of(String template, String templateId, String url, String semanticType) {
            return new TemplateInfo(template, templateId, url, semanticType );
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            TemplateInfo that = (TemplateInfo) o;
            return Objects.equals(template, that.template) && Objects.equals(templateId, that.templateId) && Objects.equals(url, that.url) && Objects.equals(semanticType, that.semanticType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(template, templateId, url, semanticType);
        }

        @Override
        public String toString() {
            return "TemplateInfo{" +
                    "template='" + template + '\'' +
                    ", templateId='" + templateId + '\'' +
                    ", url='" + url + '\'' +
                    ", semanticType='" + semanticType + '\'' +
                    '}';
        }
    }
}
