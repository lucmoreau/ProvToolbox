package org.openprovenance.prov.template.types;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class PK_CommandLineArguments {

    static Logger logger = LogManager.getLogger(PK_CommandLineArguments.class);

    public static final String HELP = "help";
    public static final String DEBUG = "debug";
    public static final String INFILE = "infile";
    public static final String OUTFILE = "outfile";
    public static final String KNOWNTYPES = "knownTypes";


    public final String debug;
    public final String infile;
    public final String outfile;
    public final String knowntypes;


    static private Options buildOptions() {

        Option help = new Option(HELP, HELP, false, "print this message");

        Option debug = new Option(DEBUG, DEBUG, false, "debug mode");


        Option infile = Option.builder("i")
                .argName("file")
                .required()
                .hasArg()
                .desc("use given file as input")
                .longOpt(INFILE)
                .build();

        Option outfile = Option.builder("o")
                .argName("file")
                .hasArg()
                .desc("use given file as output")
                .longOpt(OUTFILE)
                .build();

        Option knowntypes = Option.builder("K")
                .argName("file")
                .hasArg()
                .desc("known types")
                .longOpt(KNOWNTYPES)
                .build();

        Options options = new Options();

        options.addOption(help);
        options.addOption(debug);
        options.addOption(infile);
        options.addOption(outfile);
        options.addOption(knowntypes);

        return options;

    }

    static public PK_CommandLineArguments parse(String executable, String [] args) {

        CommandLineParser parser = new DefaultParser();
        String help = null;
        String debug = null;
        String infile = null;
        String outfile = null;
        String knowntypes = null;

        try {
            // parse the command line arguments
            Options options = buildOptions();
            System.out.println("Args " + List.of(args));

            CommandLine line = parser.parse(options, args);
            if (line.hasOption(HELP)) help = HELP;
            if (line.hasOption(DEBUG)) debug = DEBUG;
            if (line.hasOption(INFILE)) infile = line.getOptionValue(INFILE);
            if (line.hasOption(OUTFILE)) outfile = line.getOptionValue(OUTFILE);
            if (line.hasOption(KNOWNTYPES)) knowntypes = line.getOptionValue(KNOWNTYPES);


            if (help != null) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(executable, options, true);
                return null;
            }


            final PK_CommandLineArguments commandLineArguments
                    = new PK_CommandLineArguments(debug, infile, outfile, knowntypes);

            return commandLineArguments;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PK_CommandLineArguments(String debug, String infile, String outfile, String knowntypes) {
        this.debug=debug;
        this.infile=infile;
        this.outfile=outfile;
        this.knowntypes=knowntypes;
    }
}


