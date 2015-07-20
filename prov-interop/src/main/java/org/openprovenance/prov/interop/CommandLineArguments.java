package org.openprovenance.prov.interop;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.openrdf.query.algebra.Exists;

public class CommandLineArguments {
    
    public static final String BINDINGS = "bindings";
    public static final String OUTFILE = "outfile";
    public static final String VERBOSE = "verbose";
    public static final String NAMESPACES = "namespaces";
    public static final String DEBUG = "debug";
    public static final String VERSION = "version";
    public static final String HELP = "help";
    public static final String LOGFILE = "logfile";
    public static final String INFILE = "infile";
    public static final String TITLE = "title";
    public static final String LAYOUT = "layout";
    public static final String GENERATOR = "generator";
    public static final String INDEX = "index";
    public static final String COMPARE = "compare";
    public static final String FLATTEN = "flatten";
    public static final String MERGE = "merge";
    private static final String GENORDER = "genorder";
    public static final String FORMATS = "formats";
    public static final String INFORMAT = "informat";
    public static final String OUTFORMAT = "outformat";
    public static final String BINDFORMAT = "bindformat";
    public static final String COMPAREOUT = "outcompare";

    // see http://commons.apache.org/cli/usage.html
    static Options buildOptions() {

        Option help = new Option(HELP, "print this message");
        Option version = new Option(VERSION,
                "print the version information and exit");
        Option verbose = new Option(VERBOSE, "be verbose");
        Option debug = new Option(DEBUG, "print debugging information");

        Option index = new Option(INDEX, "index all elements and edges of a document, merging them where appropriate");
        Option flatten = new Option(FLATTEN, "flatten all bundles in a single document (to used with -index option or -merge option)");
         

        Option merge = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("merge all documents (listed in file argument) into a single document")
                .create(MERGE);

        Option logfile = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file for log").create(LOGFILE);

        Option infile = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file as input")
                .create(INFILE);

        Option outfile = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file as output")
                .create(OUTFILE);

        Option namespaces = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file as declaration of prefix namespaces")
                .create(NAMESPACES);

        Option bindings = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file as bindings for template expansion (template is provided as infile)")
                .create(BINDINGS);
 
        Option title = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("document title")
                .create(TITLE);

        Option layout = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("dot layout: circo, dot (default), fdp, neato, osage, sfdp, twopi ")
                .create(LAYOUT);
        
        Option generator = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("graph generator N:n:first:seed:e1")
                .create(GENERATOR);
        Option genorder = new Option(GENORDER, "In template expansion, generate order attribute. By default does not.");
        
        Option formats = new Option(FORMATS, "list supported formats");

        Option informat = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("specify the format of the input")
                .create(INFORMAT);

        Option outformat = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("specify the format of the output")
                .create(OUTFORMAT);

        Option bindformat = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("specify the format of the bindings")
                .create(BINDFORMAT);

        Option compare = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("compare with given file")
                .create(COMPARE);
        Option compareOut = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("output file for log of comparison")
                .create(COMPAREOUT);



        Options options = new Options();

        options.addOption(help);
        options.addOption(version);
        options.addOption(verbose);
        options.addOption(debug);
        options.addOption(index);
        options.addOption(merge);
        options.addOption(flatten);
        options.addOption(infile);
        options.addOption(outfile);
        options.addOption(namespaces);
        options.addOption(title);
        options.addOption(layout);
        options.addOption(bindings);
        options.addOption(generator);
        options.addOption(genorder);
        options.addOption(formats);
        options.addOption(informat);
        options.addOption(outformat);
        options.addOption(bindformat);
        options.addOption(compare);
        options.addOption(compareOut);

        return options;

    }
    
    private static String fileName = "config.properties";
    
    private static Properties getPropertiesFromClasspath(String propFileName) {
        Properties props = new Properties();
        InputStream inputStream = CommandLineArguments.class.getClassLoader()
                .getResourceAsStream(propFileName);
        if (inputStream == null) {
            return null;
        }
        try {
            props.load(inputStream);
        } catch (IOException ee) {
            return null;
        }
        return props;
    }

    
    static final String toolboxVersion = getPropertiesFromClasspath(fileName)
            .getProperty("toolbox.version");
    
    static final int STATUS_OK=0;
    static final int STATUS_PARSING_FAIL=1;
    static final int STATUS_NO_INPUT=2;
    static final int STATUS_NO_OUTPUT_OR_COMPARISON=3;
    static final int STATUS_COMPARE_NO_ARG1=4;
    static final int STATUS_COMPARE_NO_ARG2=5;
    static final int STATUS_COMPARE_DIFFERENT=6;


    public static void main(String[] args) {
        // create the parser
        CommandLineParser parser = new GnuParser();
        String help = null;      
        String version = null;
        String verbose = null;
        String debug = null;
        String logfile = null;
        String infile = null;
        String informat = null;
        String outfile = null;
        String outformat = null;
        String namespaces = null;
        String title = null;
        String layout = null;
        String bindings = null;
        String bindingformat = null;
        String generator = null;
        String index=null;
        String flatten=null;
        String merge=null;
        String compare=null;
        String compareOut=null;
        boolean addOrderp=false;
        boolean listFormatsp = false;


        try {
            // parse the command line arguments
            Options options=buildOptions();
            CommandLine line = parser.parse( options, args );

            if (line.hasOption(HELP))       help       = HELP;
            if (line.hasOption(VERSION))    version    = VERSION;
            if (line.hasOption(VERBOSE))    verbose    = VERBOSE;
            if (line.hasOption(DEBUG))      debug      = DEBUG;
            if (line.hasOption(INDEX))      index      = INDEX;
            if (line.hasOption(FLATTEN))    flatten    = FLATTEN;
            if (line.hasOption(MERGE))      merge    = line.getOptionValue(MERGE);
            if (line.hasOption(LOGFILE))    logfile    = line.getOptionValue(LOGFILE);
            if (line.hasOption(INFILE))     infile     = line.getOptionValue(INFILE);
            if (line.hasOption(INFORMAT))   informat = line.getOptionValue(INFORMAT);
            if (line.hasOption(OUTFILE))    outfile    = line.getOptionValue(OUTFILE);
            if (line.hasOption(OUTFORMAT)) outformat = line.getOptionValue(OUTFORMAT);
            if (line.hasOption(NAMESPACES)) namespaces = line.getOptionValue(NAMESPACES);
            if (line.hasOption(TITLE))      title = line.getOptionValue(TITLE);
            if (line.hasOption(LAYOUT))      layout = line.getOptionValue(LAYOUT);
            if (line.hasOption(BINDINGS))   bindings = line.getOptionValue(BINDINGS);
            if (line.hasOption(BINDFORMAT)) bindingformat = line.getOptionValue(BINDFORMAT);
            if (line.hasOption(GENERATOR))  generator = line.getOptionValue(GENERATOR);
            if (line.hasOption(GENORDER))   addOrderp=true;
            if (line.hasOption(FORMATS))      listFormatsp = true;
            if (line.hasOption(COMPARE))      compare    = line.getOptionValue(COMPARE);
            if (line.hasOption(COMPAREOUT))   compareOut    = line.getOptionValue(COMPAREOUT);

            if (help!=null) {
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp( "provconvert", options, true );
            	return;
            }

            if (version!=null) {
            	System.out.println("provconvert version " + toolboxVersion);
            	return;
            }
	    
	    
	    
            InteropFramework interop=new InteropFramework(verbose,
                                                          debug,
                                                          logfile,
                                                          infile,
                                                          informat,
                                                          outfile,
                                                          outformat,
                                                          namespaces,
                                                          title,
                                                          layout,
                                                          bindings,
                                                          bindingformat,
                                                          addOrderp,
                                                          generator,
                                                          index,
                                                          merge,
                                                          flatten,
                                                          compare,
                                                          compareOut,
                                                          org.openprovenance.prov.xml.ProvFactory.getFactory());
            if (listFormatsp) {
                java.util.List<java.util.Map<String, String>> formats = interop.getSupportedFormats();
                for (java.util.Map<String, String> e: formats) {
                    System.out.println(e.get("extension") +'\t'+ e.get("mediatype") +'\t'+ e.get("type"));
                }
                return;
            }
            System.exit(interop.run());

        }

        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            System.exit(STATUS_PARSING_FAIL);

        }
    }
}
