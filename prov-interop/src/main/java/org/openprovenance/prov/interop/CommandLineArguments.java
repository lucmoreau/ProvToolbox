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
import org.apache.log4j.Logger;

public class CommandLineArguments implements ErrorCodes {
	
	static Logger logger=Logger.getLogger(CommandLineArguments.class);
    
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
    public static final String GENORDER = "genorder";
    public static final String ALLEXPANDED = "allexpanded";
    public static final String FORMATS = "formats";
    public static final String INFORMAT = "informat";
    public static final String OUTFORMAT = "outformat";
    public static final String BINDFORMAT = "bindformat";
    public static final String COMPAREOUT = "outcompare";
    public static final String BINDINGS_VERSION = "bindver";
    public static final String TEMPLATE = "template";
    public static final String PACKAGE = "package";
    public static final String LOCATION = "location";

    public static final String BUILDER = "builder";
    public static final String TEMPLATE_BUILDER = "templatebuilder";
    public static final String LOG2PROV = "log2prov";

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
        Option allexpanded = new Option(ALLEXPANDED, "In template expansion, generate term if all variables are bound.");

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

        Option bindingsVersion = OptionBuilder
                .withArgName("int")
                .hasArg()
                .withDescription("bindings version")
                .create(BINDINGS_VERSION);

        Option template = OptionBuilder
                .withArgName("string")
                .hasArg()
                .withDescription("template name, used to create bindings bean class name")
                .create(TEMPLATE);
        
        Option builder = new Option(BUILDER, "template builder");

        Option packge = OptionBuilder
                .withArgName("package")
                .hasArg()
                .withDescription("package in which bindings bean class is generated")
                .create(PACKAGE);

        
        Option location = OptionBuilder
                .withArgName("location")
                .hasArg()
                .withDescription("location of where the template resource is to be found at runtime")
                .create(LOCATION);

        
        Option template_builder = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("template builder configuration")
                .create(TEMPLATE_BUILDER);
        
        Option log2prov = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("fully qualified ClassName of initialiser in jar file")
                .create(LOG2PROV);
        
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
        options.addOption(allexpanded);
        options.addOption(formats);
        options.addOption(informat);
        options.addOption(outformat);
        options.addOption(bindformat);
        options.addOption(compare);
        options.addOption(compareOut);
        options.addOption(bindingsVersion);
        options.addOption(template);
        options.addOption(packge);
        options.addOption(location);
        options.addOption(builder);
        options.addOption(template_builder);
        options.addOption(log2prov);

        return options;

    }
    
    private static String fileName = "config.properties";
    
    public static Properties getPropertiesFromClasspath(@SuppressWarnings("rawtypes") Class clazz, String propFileName) {
        Properties props = new Properties();
        InputStream inputStream = clazz.getResourceAsStream(propFileName);
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

    public static Properties getPropertiesFromClasspath(String propFileName) {
        return getPropertiesFromClasspath(CommandLineArguments.class, propFileName);
    }

    
    static public final String toolboxVersion = getPropertiesFromClasspath(fileName)
            .getProperty("toolbox.version");
    
    static public final String longToolboxVersion = toolboxVersion + " (" + 
            getPropertiesFromClasspath(fileName).getProperty("timestamp") + ")";
    
    public static void main(String[] args) {
        mainExit(args,true);
    }


    public static void mainExit(String[] args, boolean exit) {
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
        String template=null;
        String packge=null;
        String location=null;
        int bindingsVersion=1;
        boolean addOrderp=false;
        boolean listFormatsp = false;
        boolean allexpanded=false;
        boolean builder=false;
        String template_builder=null;
        String log2prov=null;


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
            if (line.hasOption(ALLEXPANDED)) allexpanded=true;

            if (line.hasOption(FORMATS))      listFormatsp = true;
            if (line.hasOption(COMPARE))      compare    = line.getOptionValue(COMPARE);
            if (line.hasOption(COMPAREOUT))   compareOut    = line.getOptionValue(COMPAREOUT);
            if (line.hasOption(BINDINGS_VERSION))   {
                String tmp= line.getOptionValue(BINDINGS_VERSION);
                try {
                    bindingsVersion    =new Integer(tmp);
                } catch (Exception e){
                    System.err.println("bindings version not an integer (using 1) " + tmp);
                    bindingsVersion=1;
                }
                
            }
            
            if (line.hasOption(TEMPLATE))  template = line.getOptionValue(TEMPLATE);
            if (line.hasOption(PACKAGE))  packge = line.getOptionValue(PACKAGE);
            if (line.hasOption(LOCATION))  location = line.getOptionValue(LOCATION);
            if (line.hasOption(BUILDER))  builder = true;
            if (line.hasOption(TEMPLATE_BUILDER))  template_builder = line.getOptionValue(TEMPLATE_BUILDER);
            if (line.hasOption(LOG2PROV))  log2prov = line.getOptionValue(LOG2PROV);


            if (help!=null) {
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp( "provconvert", options, true );
            	return;
            }

            if (version!=null) {
            	System.out.println("provconvert version " + longToolboxVersion);
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
                                                          bindingsVersion,
                                                          addOrderp,
                                                          allexpanded,
                                                          template,
                                                          builder,
                                                          template_builder,
                                                          packge,
                                                          location,
                                                          generator,
                                                          index,
                                                          merge,
                                                          flatten,
                                                          compare,
                                                          compareOut,
                                                          log2prov,
                                                          org.openprovenance.prov.xml.ProvFactory.getFactory());
            if (listFormatsp) {
                java.util.List<java.util.Map<String, String>> formats = interop.getSupportedFormats();
                for (java.util.Map<String, String> e: formats) {
                    System.out.println(e.get("extension") +'\t'+ e.get("mediatype") +'\t'+ e.get("type"));
                }
                return;
            }
            
            final int run = interop.run();
            if (exit) System.exit(run);

        }

        catch (ParseException exp) {
            // oops, something went wrong
        	logger.fatal("Parsing failed.  Reason: " + exp.getMessage() );
            if (exit) System.exit(STATUS_PARSING_FAIL);

        }
    }
}
