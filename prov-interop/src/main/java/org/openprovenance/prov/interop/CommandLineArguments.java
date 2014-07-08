package org.openprovenance.prov.interop;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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

    // see http://commons.apache.org/cli/usage.html
    static Options buildOptions() {

        Option help = new Option(HELP, "print this message");
        Option version = new Option(VERSION,
                "print the version information and exit");
        Option verbose = new Option(VERBOSE, "be verbose");
        Option debug = new Option(DEBUG, "print debugging information");

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

        Options options = new Options();

        options.addOption(help);
        options.addOption(version);
        options.addOption(verbose);
        options.addOption(debug);
        options.addOption(logfile);
        options.addOption(infile);
        options.addOption(outfile);
        options.addOption(namespaces);
        options.addOption(title);
        options.addOption(layout);
        options.addOption(bindings);
        options.addOption(generator);

        return options;

    }
    
    public static void main(String[] args) {
        // create the parser
        CommandLineParser parser = new GnuParser();
        String help = null;      
        String version = null;
        String verbose = null;
        String debug = null;
        String logfile = null;
        String infile = null;
        String outfile = null;
        String namespaces = null;
        String title = null;
        String layout = null;
        String bindings = null;
        String generator = null;


        try {
            // parse the command line arguments
            Options options=buildOptions();
            CommandLine line = parser.parse( options, args );

	    if (line.hasOption(HELP))       help       = HELP;
	    if (line.hasOption(VERSION))    version    = VERSION;
	    if (line.hasOption(VERBOSE))    verbose    = VERBOSE;
	    if (line.hasOption(DEBUG))      debug      = DEBUG;
	    if (line.hasOption(LOGFILE))    logfile    = line.getOptionValue(LOGFILE);
            if (line.hasOption(INFILE))     infile     = line.getOptionValue(INFILE);
	    if (line.hasOption(OUTFILE))    outfile    = line.getOptionValue(OUTFILE);
            if (line.hasOption(NAMESPACES)) namespaces = line.getOptionValue(NAMESPACES);
            if (line.hasOption(TITLE))      title = line.getOptionValue(TITLE);
            if (line.hasOption(LAYOUT))      layout = line.getOptionValue(LAYOUT);
            if (line.hasOption(BINDINGS))   bindings = line.getOptionValue(BINDINGS);
            if (line.hasOption(GENERATOR))  generator = line.getOptionValue(GENERATOR);
	    
	    if (help!=null) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "prov-convert", options, true );
		return;
	    }
	    
	    if (version!=null) {
		System.out.println("prov-convert:  version x.y.z");
		return;
	    }
	    
	    
            InteropFramework interop=new InteropFramework(verbose,
                                                          debug,
                                                          logfile,
                                                          infile,
                                                          outfile,
                                                          namespaces,
                                                          title,
                                                          layout,
                                                          bindings,
                                                          generator,
                                                          org.openprovenance.prov.xml.ProvFactory.getFactory());
            interop.run();

        }

        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
}
