package org.openprovenance.prov.interop;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.DateTimeOption;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class CommandLineArguments implements ErrorCodes {
	
	static Logger logger= LogManager.getLogger(CommandLineArguments.class);
    
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
    public static final String QUALIFIED = "qualified";
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
    public static final String LOG2KERNEL = "log2kernel";
    public static final String CONFIG = "ttfs";
    public static final String DATE_TIME = "dateTime";
    public static final String TIMEZONE = "timeZone";
    public static final String TEMPLATE_LIBRARY = "templateLibrary";
    public static final String INPUT_BASED_DIR="inputBaseDir";
    public static final String OUTPUT_BASED_DIR="outputBaseDir";

    public static final String METRICS = "metrics";

    // see http://commons.apache.org/cli/usage.html
    static Options buildOptions() {

        Option help = new Option(HELP, HELP, false, "print this message");
        Option version = new Option(VERSION,VERSION, false, "print the version information and exit");
        Option verbose = new Option(VERBOSE, VERBOSE, false, "be verbose");
        Option debug = new Option(DEBUG, DEBUG, false, "print debugging information");

        Option index = new Option(INDEX, INDEX, false, "index all elements and edges of a document, merging them where appropriate");
        Option flatten = new Option(FLATTEN, FLATTEN, false, "flatten all bundles in a single document (to used with -index option or -merge option)");
         

        Option merge = Option.builder(MERGE)
                .argName("file")
                .hasArg()
                .desc("merge all documents (listed in file argument) into a single document")
                .longOpt(MERGE).build();

        Option logfile = Option.builder(LOGFILE)
                .argName("file")
                .hasArg()
                .desc("use given file for log").longOpt(LOGFILE).build();

        Option infile = Option.builder(INFILE)
                .argName("file")
                .hasArg()
                .desc("use given file as input")
                .longOpt(INFILE)
                .build();

        Option outfile = Option.builder(OUTFILE)
                .argName("file")
                .hasArg()
                .desc("use given file as output")
                .longOpt(OUTFILE)
                .build();

        Option namespaces = Option.builder(NAMESPACES)
                .argName("file")
                .hasArg()
                .desc("use given file as declaration of prefix namespaces")
                .longOpt(NAMESPACES)
                .build();

        Option bindings = Option.builder(BINDINGS)
                .argName("file")
                .hasArg()
                .desc("use given file as bindings for template expansion (template is provided as infile)")
                .longOpt(BINDINGS)
                .build();
 
        Option title = Option.builder(TITLE)
                .argName("string")
                .hasArg()
                .desc("document title")
                .longOpt(TITLE)
                .build();

        Option layout = Option.builder(LAYOUT)
                .argName("string")
                .hasArg()
                .desc("dot layout: circo, dot (default), fdp, neato, osage, sfdp, twopi ")
                .longOpt(LAYOUT)
                .build();
        
        Option generator = Option.builder(GENERATOR)
                .argName("string")
                .hasArg()
                .desc("graph generator N:n:first:seed:e1")
                .longOpt(GENERATOR)
                .build();
        Option genorder = new Option(GENORDER, GENORDER, false, "In template expansion, generate order attribute. By default does not.");
        Option allexpanded = new Option(ALLEXPANDED, ALLEXPANDED, false, "In template expansion, generate term if all variables are bound.");

        Option formats = new Option(FORMATS, FORMATS, false, "list supported formats");
        Option displayQualifiedRelation = new Option(QUALIFIED, QUALIFIED, false, "display qualified relations in dot output");

        Option informat = Option.builder(INFORMAT)
                .argName("string")
                .hasArg()
                .desc("specify the format of the input")
                .longOpt(INFORMAT)
                .build();

        Option outformat = Option.builder(OUTFORMAT)
                .argName("string")
                .hasArg()
                .desc("specify the format of the output")
                .longOpt(OUTFORMAT)
                .build();

        Option bindformat = Option.builder(BINDFORMAT)
                .argName("string")
                .hasArg()
                .desc("specify the format of the bindings")
                .longOpt(BINDFORMAT)
                .build();

        Option compare = Option.builder(COMPARE)
                .argName("file")
                .hasArg()
                .desc("compare with given file")
                .longOpt(COMPARE)
                .build();
        Option compareOut = Option.builder(COMPAREOUT)
                .argName("file")
                .hasArg()
                .desc("output file for log of comparison")
                .longOpt(COMPAREOUT)
                .build();

        Option bindingsVersion = Option.builder(BINDINGS_VERSION)
                .argName("int")
                .hasArg()
                .desc("bindings version")
                .longOpt(BINDINGS_VERSION)
                .build();

        Option template = Option.builder(TEMPLATE)
                .argName("string")
                .hasArg()
                .desc("template name, used to create bindings bean class name")
                .longOpt(TEMPLATE)
                .build();
        
        Option builder = new Option(BUILDER, BUILDER, false, "template builder");

        Option packge = Option.builder(PACKAGE)
                .argName("package")
                .hasArg()
                .desc("package in which bindings bean class is generated")
                .longOpt(PACKAGE)
                .build();

        
        Option location = Option.builder(LOCATION)
                .argName("location")
                .hasArg()
                .desc("location of where the template resource is to be found at runtime")
                .longOpt(LOCATION)
                .build();

        
        Option template_builder = Option.builder(TEMPLATE_BUILDER)
                .argName("file")
                .hasArg()
                .desc("template builder configuration")
                .longOpt(TEMPLATE_BUILDER)
                .build();

        Option log2prov = Option.builder(LOG2PROV)
                .argName("file")
                .hasArg()
                .desc("fully qualified ClassName of initialiser in jar file")
                .longOpt(LOG2PROV)
                .build();

        Option dateTime = Option.builder(DATE_TIME)
                .argName("dateTime")
                .hasArg()
                .desc("date time options PRESERVE, UTC, SYSTEM, TIMEZONE")
                .longOpt(DATE_TIME)
                .build();

        Option timeZone = Option.builder(TIMEZONE)
                .argName("timeZone")
                .hasArg()
                .desc("a timezone")
                .longOpt(TIMEZONE)
                .build();

        Option metrics = Option.builder(METRICS)
                .argName("file")
                .hasArg()
                .desc("use given file as metrics output")
                .longOpt(METRICS)
                .build();

        Option log2kernel = new Option(LOG2KERNEL, LOG2KERNEL, false, "generating provenance types");


        Option config = new Option(CONFIG, CONFIG, false, "get configuration");

        Option templateLibrary = Option.builder(TEMPLATE_LIBRARY)
                .argName("path")
                .hasArgs()
                .desc("add given path(s) to template library paths")
                .longOpt(TEMPLATE_LIBRARY)
                .build();
        Option inputBasedDir = Option.builder(INPUT_BASED_DIR)
                .argName("directory")
                .hasArg()
                .desc("input base directory for relative paths")
                .longOpt(INPUT_BASED_DIR)
                .build();

        Option outputBasedDir = Option.builder(OUTPUT_BASED_DIR)
                .argName("directory")
                .hasArg()
                .desc("output base directory for relative paths")
                .longOpt(OUTPUT_BASED_DIR)
                .build();


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
        options.addOption(displayQualifiedRelation);
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
        options.addOption(log2kernel);
        options.addOption(config);
        options.addOption(dateTime);
        options.addOption(timeZone);
        options.addOption(metrics);
        options.addOption(templateLibrary);
        options.addOption(outputBasedDir);
        options.addOption(inputBasedDir);

        return options;

    }


    public static void main(String[] args) {
        mainExit(args,true);
    }

    
    public static void mainNoExit(String[] args) {
        mainExit(args,false);
    }

    public static void mainExit(String[] args, boolean exit) {
        // create the parser
        CommandLineParser parser = new DefaultParser();
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
        int bindingsVersion=3; // change default is now 3
        boolean addOrderp=false;
        boolean listFormatsp = false;
        boolean displayQualifiedRelation=false;
        boolean allexpanded=false;
        boolean builder=false;
        String template_builder=null;
        String log2prov=null;
        boolean log2kernel=false;
        boolean config=false;
        DateTimeOption dateTime=DateTimeOption.UTC;
        TimeZone timeZone=null;
        String metrics=null;
        List<String> templateLibraryPath = null;
        String inputBaseDir=null;
        String outputBaseDir=null;



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
            if (line.hasOption(QUALIFIED))    displayQualifiedRelation=true;
            if (line.hasOption(COMPARE))      compare    = line.getOptionValue(COMPARE);
            if (line.hasOption(COMPAREOUT))   compareOut    = line.getOptionValue(COMPAREOUT);
            if (line.hasOption(BINDINGS_VERSION))   {
                String tmp= line.getOptionValue(BINDINGS_VERSION);
                try {
                    bindingsVersion  = Integer.valueOf(tmp);
                } catch (Exception e){
                    System.err.println("bindings version not an integer (using 3) " + tmp);
                    bindingsVersion=3;
                }
                
            }
            
            if (line.hasOption(TEMPLATE))  template = line.getOptionValue(TEMPLATE);
            if (line.hasOption(PACKAGE))  packge = line.getOptionValue(PACKAGE);
            if (line.hasOption(LOCATION))  location = line.getOptionValue(LOCATION);
            if (line.hasOption(BUILDER))  builder = true;
            if (line.hasOption(TEMPLATE_BUILDER))  template_builder = line.getOptionValue(TEMPLATE_BUILDER);
            if (line.hasOption(LOG2PROV))  log2prov = line.getOptionValue(LOG2PROV);
            if (line.hasOption(LOG2KERNEL))  log2kernel = true;

            if (line.hasOption(CONFIG))  config = true;

            if (line.hasOption(METRICS))  metrics = line.getOptionValue(METRICS);

            if (help!=null) {
            	HelpFormatter formatter = new HelpFormatter();
            	formatter.printHelp( "provconvert", options, true );
            	return;
            }

            if (version!=null) {
            	System.out.println("provconvert version " + Configuration.longToolboxVersion);
            	return;
            }

            if (line.hasOption(DATE_TIME)) dateTime = DateTimeOption.valueOf(line.getOptionValue(DATE_TIME));

            if (line.hasOption(TIMEZONE)) {
                try {
                    timeZone = TimeZone.getTimeZone(line.getOptionValue(TIMEZONE));
                } catch (Throwable e) {
                    System.err.println("timeZone not recognised " + line.getOptionValue(TIMEZONE));
                    logger.warn("timeZone not recognised " + line.getOptionValue(TIMEZONE));
                    logger.throwing(e);
                }
            }

            if (line.hasOption(TEMPLATE_LIBRARY)) {
                String[] vals = line.getOptionValues(TEMPLATE_LIBRARY);
                if (vals != null) {
                    templateLibraryPath = java.util.Arrays.asList(vals);
                }
            }
            if (line.hasOption(INPUT_BASED_DIR)) {
                inputBaseDir = line.getOptionValue(INPUT_BASED_DIR);
            }
            if (line.hasOption(OUTPUT_BASED_DIR)) {
                outputBaseDir = line.getOptionValue(OUTPUT_BASED_DIR);
            }

            final CommandLineArguments commandLineArguments
                    = new CommandLineArguments( verbose,
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
                                                log2kernel,
                                                config,
                                                dateTime,
                                                timeZone,
                                                metrics,
                                                displayQualifiedRelation,
                                                inputBaseDir,
                                                outputBaseDir,
                                                templateLibraryPath);
            InteropFramework interop=new InteropFramework(commandLineArguments,
                                                          InteropFramework.getDefaultFactory());
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
    

    public final  String verbose;
    public final  String debug;
    public final  String logfile;
    public final  String infile;
    public final  String informat;
    public final  String outfile;
    public final  String outformat;
    public final  String namespaces;
    public final  String title;
    public final  String layout;
    public final  String bindings;
    public final  String bindingformat;
    public final  int bindingsVersion;
    public final  boolean addOrderp;
    public final  boolean allExpanded;
    public final  String template;
    public final  boolean builder;
    public final  String template_builder;
    public final  String packge;
    public final  String location;
    public final  String generator;
    public final  String index;
    public final  String merge;
    public final  String flatten;
    public final  String compare;
    public final  String compareOut;
    public final  String log2prov;
    public final  boolean log2kernel;
    public final  boolean config;
    public final DateTimeOption dateTime;
    public final TimeZone timeZone;
    public final String metrics;
    public final boolean displayQualifiedRelation;
    public List<String> templateLibraryPath = null;
    public String inputBaseDir=null;
    public String outputBaseDir=null;

    public CommandLineArguments(String verbose, String debug, String logfile,
                                String infile, String informat, String outfile, String outformat, String namespaces, String title,
                                String layout, String bindings, String bindingformat, int bindingsVersion, boolean addOrderp, boolean allExpanded, String template, boolean builder, String template_builder, String packge, String location, String generator,
                                String index, String merge, String flatten, String compare, String compareOut, String log2prov, boolean log2kernel, boolean config, DateTimeOption dateTime, TimeZone timeZone, String metrics, boolean displayQualifiedRelation,
                                String inputBaseDir, String outputBaseDir, List<String> templateLibraryPath) {
        this.verbose=verbose;
        this.debug=debug;
        this.logfile=logfile;
        this.infile=infile;
        this.informat=informat;
        this.outfile=outfile;
        this.outformat=outformat;
        this.namespaces=namespaces;
        this.title=title;
        this.layout=layout;
        this.bindings=bindings;
        this.bindingformat=bindingformat;
        this.bindingsVersion=bindingsVersion;
        this.addOrderp=addOrderp;
        this.allExpanded=allExpanded;
        this.template=template;
        this.builder=builder;
        this.template_builder=template_builder;
        this.packge=packge;
        this.location=location;
        this.generator=generator;
        this.index=index;
        this.merge=merge;
        this.flatten=flatten;
        this.compare=compare;
        this.compareOut=compareOut;
        this.log2prov=log2prov;
        this.log2kernel=log2kernel;
        this.config=config;
        this.dateTime=dateTime;
        this.timeZone=timeZone;
        this.metrics=metrics;
        this.displayQualifiedRelation=displayQualifiedRelation;
        this.inputBaseDir=inputBaseDir;
        this.outputBaseDir=outputBaseDir;
        this.templateLibraryPath=templateLibraryPath;
        
    }
    public CommandLineArguments() {
        this.verbose = null;
        this.debug = null;
        this.logfile = null;
        this.infile = null;
        this.informat = null;
        this.outfile = null;
        this.outformat = null;
        this.namespaces = null;
        this.title = null;
        this.layout = null;
        this.bindings = null;
        this.bindingformat = null;
        this.generator = null;
        this.index=null;
        this.flatten=null;
        this.merge=null;
        this.compare=null;
        this.compareOut=null;
        this.template=null;
        this.packge=null;
        this.location=null;
        this.bindingsVersion=3;
        this.addOrderp=false;
        this.allExpanded=false;
        this.builder=false;
        this.template_builder=null;
        this.log2prov=null;
        this.log2kernel=false;
        this.config=false;
        this.dateTime=DateTimeOption.PRESERVE;
        this.timeZone=null;
        this.metrics=null;
        this.displayQualifiedRelation=false;
        this.inputBaseDir=null;
        this.outputBaseDir=null;
        this.templateLibraryPath=new LinkedList<>();
    }
    
}
