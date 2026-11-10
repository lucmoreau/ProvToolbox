package org.openprovenance.prov.template.compiler.privacy;

import org.apache.commons.text.StringEscapeUtils;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** T31 (R12): writes the privacy register as JSON (for tools and audits) and as an HTML page (for people). */
public class PrivacyRegisterWriter {

    public static final String JSON = "privacy-register.json";
    public static final String HTML = "privacy-register.html";

    private final ObjectMapper om = JsonMapper.builderWithJackson2Defaults()
            .configure(SerializationFeature.INDENT_OUTPUT, true).build();

    public void write(List<RegisterEntry> rows, File dir) throws IOException {
        om.writeValue(new File(dir, JSON), rows);
        try (PrintStream os = new PrintStream(new File(dir, HTML), StandardCharsets.UTF_8)) {
            os.println("<html>");
            os.println("<head>");
            os.println(" <meta content=\"text/html;charset=utf-8\" http-equiv=\"Content-Type\"/>");
            os.println(" <link rel='stylesheet' href='../css/provtemplate.css'>");
            os.println(" <title>Privacy register</title>");
            os.println("</head>");
            os.println("<body>");
            os.println("<h1>Privacy register</h1>");
            os.println("<p>Each template variable with a data-protection annotation, as the library declared it and as it is in effect"
                    + " in this generated code. Fixed when the code was generated: the digests are those in the database constraints'"
                    + " comments and in the generated code's <code>privacy</code> constants.</p>");
            os.println("<table class='privacy_register'>");
            os.println("<tr><th>Template</th><th>Variable</th><th>Declared</th><th>In effect</th><th>Decided by</th><th>Reason</th>"
                    + "<th>Defined by</th><th>Kept in / form</th><th>Resolver</th><th>Digest</th><th>Note</th></tr>");
            for (RegisterEntry r : rows) {
                String keptOrForm = r.keeps != null ? (r.keeps.isEmpty() ? "nowhere" : String.join(", ", r.keeps))
                        : r.pattern != null ? "<code>" + e(r.pattern) + "</code>" : "";
                os.println("<tr><td>" + e(r.template) + "</td><td>" + e(r.variable) + "</td><td>" + e(r.declared) + "</td><td>"
                        + e(r.effective) + "</td><td>" + e(r.decidedBy) + "</td><td>" + e(r.reason) + "</td><td>" + e(r.definedBy)
                        + "</td><td>" + keptOrForm + "</td><td>" + e(r.resolver) + "</td><td><code>" + e(r.digest)
                        + "</code></td><td>" + e(r.note) + "</td></tr>");
            }
            os.println("</table>");
            os.println("</body>");
            os.println("</html>");
        }
    }

    private static String e(String s) {
        return s == null ? "" : StringEscapeUtils.escapeHtml4(s);
    }
}
