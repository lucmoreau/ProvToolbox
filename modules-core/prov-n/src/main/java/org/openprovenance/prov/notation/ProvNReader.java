package org.openprovenance.prov.notation;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.exception.UncheckedException;

/**
 * Reads PROV-N documents with the ANTLR 4 parser ({@link ProvNParser}, generated from ProvN.g4), and
 * constructs their PROV representation through a {@link ModelConstructor} (see {@link ProvNTreeTraversal}).
 * <p>
 * It is meant to replace the reading methods of {@link Utility}, which rely on the ANTLR 3 parser, and it
 * constructs the same representation. Unlike them, it does not attempt to recover from syntax errors:
 * a document that is not valid PROV-N is rejected with a {@link ProvNSyntaxException} that lists all errors.
 * <p>
 * Text is decoded as UTF-8. Instances hold no parsing state, and may be used concurrently.
 */
public class ProvNReader {

    private final ProvFactory pFactory;
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    public ProvNReader(ProvFactory pFactory) {
        this(pFactory, DateTimeOption.PRESERVE, null);
    }

    public ProvNReader(ProvFactory pFactory, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.pFactory = pFactory;
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
    }

    public Document readDocument(String filename) {
        try {
            return readDocument(CharStreams.fromFileName(filename, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public Document readDocument(InputStream is) {
        try {
            return readDocument(CharStreams.fromStream(is, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public Document readDocument(Reader reader) {
        try {
            return readDocument(CharStreams.fromReader(reader));
        } catch (IOException e) {
            throw new UncheckedException(e);
        }
    }

    public Document readDocumentFromString(String provn) {
        return readDocument(CharStreams.fromString(provn));
    }

    public Document readDocument(CharStream input) {
        return (Document) convert(parse(input), pFactory);
    }

    /**
     * Constructs the PROV representation of a parse tree, with the given {@link ModelConstructor}.
     * @param tree the parse tree of a document, as returned by {@link #parse(CharStream)}
     * @param c the model constructor to invoke
     * @return the object that c constructs for the document
     */
    public Object convert(ProvNParser.DocumentContext tree, ModelConstructor c) {
        return new ProvNTreeTraversal(c, pFactory, dateTimeOption, optionalTimeZone).visitDocument(tree);
    }

    /**
     * Parses a PROV-N document.
     * <p>
     * The parser first tries the faster SLL prediction mode, which suffices for virtually all inputs, and
     * resorts to full LL prediction only if this fails, which also gives accurate error messages.
     * @param input the PROV-N text
     * @return the parse tree of the document
     * @throws ProvNSyntaxException if the input is not syntactically valid
     */
    public ProvNParser.DocumentContext parse(CharStream input) {
        ErrorCollector errors = new ErrorCollector();

        ProvNLexer lexer = new ProvNLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errors);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ProvNParser parser = new ProvNParser(tokens);
        parser.removeErrorListeners();
        parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
        parser.setErrorHandler(new BailErrorStrategy());

        ProvNParser.DocumentContext tree;
        try {
            tree = parser.document();
        } catch (ParseCancellationException e) {
            tokens.seek(0);
            parser.reset();
            parser.addErrorListener(errors);
            parser.setErrorHandler(new DefaultErrorStrategy());
            parser.getInterpreter().setPredictionMode(PredictionMode.LL);
            tree = parser.document();
        }
        if (!errors.messages.isEmpty()) {
            throw new ProvNSyntaxException(input.getSourceName(), errors.messages);
        }
        return tree;
    }

    static class ErrorCollector extends BaseErrorListener {
        final List<String> messages = new ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
                                String msg, RecognitionException e) {
            messages.add("line " + line + ":" + charPositionInLine + " " + msg);
        }
    }
}
