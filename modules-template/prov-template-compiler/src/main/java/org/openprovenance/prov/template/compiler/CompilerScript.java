package org.openprovenance.prov.template.compiler;

import org.openprovenance.prov.template.compiler.common.Constants;
import org.openprovenance.prov.template.compiler.configuration.TemplatesCompilerConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermissions;


public class CompilerScript {
    private final ConfigProcessor configProcessor;

    public CompilerScript(ConfigProcessor configProcessor) {
        this.configProcessor = configProcessor;
    }





    public void generateScript(TemplatesCompilerConfig configs) {
        new File(configs.script_dir).mkdirs();
        try {
            final String path = configs.script_dir + "/" + configs.script;
            PrintStream os = new PrintStream(path);
            InputStream in = configProcessor.getClass().getResourceAsStream("/script.sh");

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while (line != null) {
                line = line.replace("${COMMENTS}", "generated by " + CompilerScript.class + ".generateScript");
                line = line.replace("${SCRIPT}", configs.script);
                line = line.replace("${VERSION}", configs.version);
                line = line.replace("${NAME}", configs.name);
                line = line.replace("${GROUP}", configs.group.replace(".", "/"));
                line = line.replace("${INIT}", configs.init_package + "." + Constants.INIT);

                os.println(line);
                line = reader.readLine();
            }
            os.close();
            in.close();
            Files.setPosixFilePermissions(new File(path).toPath(), PosixFilePermissions.fromString("rwxr-xr-x"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }








}