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
    public static final String KNOWNRELATIONS = "knownRelations";
    public static final String RELATION_OFFSET ="relationOffset";
    public static final String SET_OFFSET ="setOffset";
    public static final String LEVEL_OFFSET ="levelOffset";


    public final String debug;
    public final String infile;
    public final String outfile;
    public final String knowntypes;
    public final String knownrelations;
    public final int relationOffset;
    public final int setOffset;
    public final int levelOffset;


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

        Option knowntypes = Option.builder("T")
                .argName("file")
                .hasArg()
                .desc("known types")
                .longOpt(KNOWNTYPES)
                .build();

        Option knownrelations = Option.builder("R")
                .argName("file")
                .hasArg()
                .desc("known types")
                .longOpt(KNOWNRELATIONS)
                .build();

        Option relationOffset = Option.builder("r")
                .argName("int")
                .hasArg()
                .desc("relation offset")
                .longOpt(RELATION_OFFSET)
                .build();
        Option setOffset = Option.builder("s")
                .argName("int")
                .hasArg()
                .desc("set offset")
                .longOpt(SET_OFFSET)
                .build();
        Option levelOffset = Option.builder("l")
                .argName("int")
                .hasArg()
                .desc("level offset")
                .longOpt(LEVEL_OFFSET)
                .build();

        Options options = new Options();

        options.addOption(help);
        options.addOption(debug);
        options.addOption(infile);
        options.addOption(outfile);
        options.addOption(knowntypes);
        options.addOption(knownrelations);
        options.addOption(setOffset);
        options.addOption(relationOffset);
        options.addOption(levelOffset);

        return options;

    }

    static public PK_CommandLineArguments parse(String executable, String [] args) {

        CommandLineParser parser = new DefaultParser();
        String help = null;
        String debug = null;
        String infile = null;
        String outfile = null;
        String knowntypes = null;
        String knownrelations = null;

        int relationOffset=2000;
        int setOffset=100000;
        int levelOffset=1000000;

        try {
            // parse the command line arguments
            Options options = buildOptions();
            System.out.println("Args " + List.of(args));

            CommandLine line = parser.parse(options, args);
            if (line.hasOption(HELP))               help = HELP;
            if (line.hasOption(DEBUG))              debug = DEBUG;
            if (line.hasOption(INFILE))             infile = line.getOptionValue(INFILE);
            if (line.hasOption(OUTFILE))            outfile = line.getOptionValue(OUTFILE);
            if (line.hasOption(KNOWNTYPES))         knowntypes = line.getOptionValue(KNOWNTYPES);
            if (line.hasOption(KNOWNRELATIONS))     knownrelations = line.getOptionValue(KNOWNRELATIONS);
            if (line.hasOption(SET_OFFSET))         setOffset = Integer.parseInt(line.getOptionValue(SET_OFFSET));
            if (line.hasOption(RELATION_OFFSET))    relationOffset = Integer.parseInt(line.getOptionValue(SET_OFFSET));
            if (line.hasOption(LEVEL_OFFSET))       levelOffset = Integer.parseInt(line.getOptionValue(LEVEL_OFFSET));


            if (help != null) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(executable, options, true);
                return null;
            }


            final PK_CommandLineArguments commandLineArguments
                    = new PK_CommandLineArguments(debug, infile, outfile, knowntypes, knownrelations, setOffset, relationOffset, levelOffset);

            return commandLineArguments;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PK_CommandLineArguments(String debug, String infile, String outfile, String knowntypes, String knownrelations, int setOffset, int relationOffset, int levelOffset) {
        this.debug=debug;
        this.infile=infile;
        this.outfile=outfile;
        this.knowntypes=knowntypes;
        this.knownrelations=knownrelations;
        this.setOffset=setOffset;
        this.relationOffset=relationOffset;
        this.levelOffset=levelOffset;
    }
}


