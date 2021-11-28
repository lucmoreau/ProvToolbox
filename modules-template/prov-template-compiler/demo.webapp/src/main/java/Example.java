
import java.util.*;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;

import org.example.templates.block.Template_blockBuilderTypeManagement;
import org.example.templates.block.client.Template_blockBuilder;
import org.example.templates.block.client.Template_blockBean;
import org.example.templates.client.Logger;
import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.vanilla.ProvFactory;

import static java.util.stream.Collectors.toList;

public class Example {
    public static void main(String[] args) throws Exception {
        final Logger logger = new Logger();

        List<Template_blockBean> beans=Arrays.asList(
                logger.beanTemplate_block("operation/01", "Operation1", "Luc",
                        "entity/e1", "value1", "entity/e2", 2,
                        "entity/e3", "Type2", 3),
                logger.beanTemplate_block("operation/02", "Operation1", "Luc",
                        "entity/e4", "value4", "entity/e3", 3,
                        "entity/e5", "Type5", 5),
                logger.beanTemplate_block("operation/03", "Operation1", "Luc",
                        "entity/e6", "value6", "entity/e5", 5,
                        "entity/e7", "Type7", 7)
        );

        final Template_blockBuilder template_blockBuilder = new Template_blockBuilder();
        List<String> log=beans.stream().map(bean -> bean.process(template_blockBuilder.aArgs2CsVConverter)).collect(toList());

        PrintStream ps=new PrintStream(new FileOutputStream(new File(args[0])));

        log.stream().forEach(s -> ps.println(s));

        Builder[]  builders=logger.getBuilders();

        Object [] record=beans.get(0).process(template_blockBuilder.aArgs2RecordConverter());

        final int input = 4;
        StringBuffer inputType=new StringBuffer().append(3333);

        StringBuffer sb=new StringBuffer();
        constructType(sb, template_blockBuilder, input, inputType);
        System.out.println(sb);


        final int input2 = 6;
        StringBuffer input2Type=new StringBuffer().append(4444);

        StringBuffer sb2=new StringBuffer();
        constructType(sb2, template_blockBuilder, input2, input2Type);
        System.out.println(sb2);

        System.out.println("end");

        getLevel0(beans, template_blockBuilder);

        System.out.println("end2");




    }

    public static void getLevel0(List<Template_blockBean> beans, Template_blockBuilder template_blockBuilder) {
        ProvFactory pf=ProvFactory.getFactory();

        Map<QualifiedName, Set<String>> knownTypeMap=new HashMap<>();
        Map<QualifiedName, Set<String>> unknownTypeMap=new HashMap<>();
        beans.forEach(bean -> {
            Object [] arecord=bean.process(template_blockBuilder.aArgs2RecordConverter());
            String s=new org.example.templates.block.Template_blockBuilder(pf).make(arecord,new Template_blockBuilderTypeManagement<>(knownTypeMap,unknownTypeMap));
        });

        System.out.println("Known Type Map");
        knownTypeMap.forEach((k,v) -> {if (k!=null) System.out.println(k.getPrefix()+":"+ k.getLocalPart()+ " -> " + v);});
        System.out.println("Unknown Type Map");
        unknownTypeMap.forEach((k,v) -> {if (k!=null) System.out.println(k.getPrefix()+":"+ k.getLocalPart() + " -> " + v);});
    }

    public static void constructType(StringBuffer sb, Template_blockBuilder template_blockBuilder, int in, StringBuffer inType) {
        int[] tt= template_blockBuilder.getTypedSuccessors().get(in);
        constructType(sb, template_blockBuilder, in,inType, tt[0], tt[1]);
    }

    static void constructType(StringBuffer sb, Template_blockBuilder template_blockBuilder, int in, StringBuffer inType, int out, int outType) {
        sb.append(template_blockBuilder.getName());
        sb.append(".");
        sb.append(template_blockBuilder.propertyOrder[out]);
        sb.append(".");
        sb.append(niceRelationName(StatementOrBundle.Kind.values()[outType]));
        sb.append(".");
        sb.append(template_blockBuilder.propertyOrder[in]);
        sb.append("(");
        sb.append(inType);
        sb.append(")");



    }

    private static String niceRelationName(StatementOrBundle.Kind value) {
        switch (value) {
            case PROV_ENTITY:
                return "ent";
            case PROV_ACTIVITY:
                return "act";
            case PROV_AGENT:
                return "ag";
            case PROV_USAGE:
                return "usd";
            case PROV_GENERATION:
                return "wgb";
            case PROV_INVALIDATION:
                return "wib";
            case PROV_START:
                return "wsb";
            case PROV_END:
                return "web";
            case PROV_COMMUNICATION:
                return "winf";
            case PROV_DERIVATION:
                return "wdf";
            case PROV_ASSOCIATION:
                return "waw";
            case PROV_ATTRIBUTION:
                return "wat";
            case PROV_DELEGATION:
                return "aobo";
            case PROV_INFLUENCE:
                return "winfl";
            case PROV_ALTERNATE:
                return "alt";
            case PROV_SPECIALIZATION:
                return "spec";
            case PROV_MENTION:
                return "mention";
            case PROV_MEMBERSHIP:
                return "mem";
            case PROV_BUNDLE:
                return "bun";
            case PROV_DICTIONARY_INSERTION:
            case PROV_DICTIONARY_REMOVAL:
            case PROV_DICTIONARY_MEMBERSHIP:
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }
}