package org.openprovenance.prov.interop;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArguments {
    
    public static final String NAMESPACES = "namespaces";
    public static final String DEBUG = "debug";
    public static final String VERSION = "version";
    public static final String HELP = "help";
    public static final String LOGFILE = "logfile";
    public static final String INFILE = "infile";

    // see http://commons.apache.org/cli/usage.html
    static Options buildOptions() {

        Option help = new Option(HELP, "print this message");
        Option version = new Option(VERSION,
                "print the version information and exit");
        Option verbose = new Option("verbose", "be verbose");
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

        Option namespaces = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("use given file as declaration of prefix namespaces")
                .create(NAMESPACES);

        Options options = new Options();

        options.addOption(help);
        options.addOption(version);
        options.addOption(verbose);
        options.addOption(debug);
        options.addOption(logfile);
        options.addOption(infile);
        options.addOption(namespaces);

        return options;

    }
    
    public static void main(String[] args) {
        // create the parser
        CommandLineParser parser = new GnuParser();
        String infile;
        try {
            // parse the command line arguments
            CommandLine line = parser.parse( buildOptions(), args );
            
            

            if (line.hasOption(HELP)) infile = line.getOptionValue(HELP);
            if (line.hasOption(VERSION)) infile = line.getOptionValue(VERSION);
            if (line.hasOption(DEBUG)) infile = line.getOptionValue(DEBUG);
            if (line.hasOption(LOGFILE)) infile = line.getOptionValue(LOGFILE);
            if (line.hasOption(INFILE)) infile = line.getOptionValue(INFILE);
            if (line.hasOption(NAMESPACES)) infile = line.getOptionValue(NAMESPACES);
        }

        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
}
