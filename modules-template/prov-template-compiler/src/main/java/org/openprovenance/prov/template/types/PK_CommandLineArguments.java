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
    public static final String TRANSLATION ="translation";
    public static final String LEVEL_NUMBER ="levelNumber";
    public static final String ADD_LEVEL0 ="addLevel0ToAllLevels";
    public static final String PROPERTY_CONVERTERS ="propertyConverters";
    public static final String REJECTED_TYPES ="rejectedTypes";


    public final String debug;
    public final String infile;
    public final String outfile;
    public final String knowntypes;
    public final String knownrelations;
    public final int relationOffset;
    public final int setOffset;
    public final int levelOffset;
    public final int levelNumber;
    public final String translation;
    public final boolean addLevel0ToAllLevels;
    public String propertyConverters;
    public String rejectedTypes;


    static private Options buildOptions() {

        Option help = new Option(HELP, HELP, false, "print this message");

        Option debug = new Option(DEBUG, DEBUG, false, "debug mode");

        Option addLevel0 = new Option(ADD_LEVEL0, ADD_LEVEL0, false, "add level0 to all levels");

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
        Option translation = Option.builder("t")
                .argName("file")
                .hasArg()
                .desc("relation translation for pretty printing of types")
                .longOpt(TRANSLATION)
                .build();
        Option levelNumber = Option.builder("L")
                .argName("int")
                .hasArg()
                .desc("level number")
                .longOpt(LEVEL_NUMBER)
                .build();


        Option propertyConverters = Option.builder("p")
                .argName("files")
                .hasArg()
                .desc("property converters ")
                .longOpt(PROPERTY_CONVERTERS)
                .build();


        Option rejectedTypes = Option.builder("K")
                .argName("files")
                .hasArg()
                .desc("rejected types ")
                .longOpt(REJECTED_TYPES)
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
        options.addOption(translation);
        options.addOption(levelNumber);
        options.addOption(addLevel0);
        options.addOption(propertyConverters);
        options.addOption(rejectedTypes);

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
        String translation = null;

        int relationOffset=2000;
        int setOffset=100000;
        int levelOffset=1000000;
        int levelNumber=4;
        boolean addLevel0=false;
        String propertyConverters=null;
        String rejectedTypesFile=null;

        try {
            // parse the command line arguments
            Options options = buildOptions();
	    //            System.out.println("Args " + List.of(args));

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
            if (line.hasOption(LEVEL_NUMBER))       levelNumber = Integer.parseInt(line.getOptionValue(LEVEL_NUMBER));
            if (line.hasOption(TRANSLATION))        translation =  line.getOptionValue(TRANSLATION);
            if (line.hasOption(PROPERTY_CONVERTERS)) propertyConverters=line.getOptionValue(PROPERTY_CONVERTERS);
            if (line.hasOption(REJECTED_TYPES))      rejectedTypesFile=line.getOptionValue(REJECTED_TYPES);

            if (line.hasOption(ADD_LEVEL0))         addLevel0 = true;

            if (help != null) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(executable, options, true);
                return null;
            }


            final PK_CommandLineArguments commandLineArguments
                    = new PK_CommandLineArguments(debug, infile, outfile, knowntypes, knownrelations, setOffset, relationOffset, levelOffset, levelNumber, translation, addLevel0, propertyConverters, rejectedTypesFile);

            return commandLineArguments;


        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PK_CommandLineArguments(String debug, String infile, String outfile, String knowntypes, String knownrelations, int setOffset, int relationOffset, int levelOffset, int levelNumber, String translation, boolean addLevel0, String propertyConverters, String rejectedTypesFile) {
        this.debug=debug;
        this.infile=infile;
        this.outfile=outfile;
        this.knowntypes=knowntypes;
        this.knownrelations=knownrelations;
        this.setOffset=setOffset;
        this.relationOffset=relationOffset;
        this.levelOffset=levelOffset;
        this.translation=translation;
        this.levelNumber=levelNumber;
        this.addLevel0ToAllLevels=addLevel0;
        this.propertyConverters=propertyConverters;
        this.rejectedTypes=rejectedTypesFile;
    }
}


