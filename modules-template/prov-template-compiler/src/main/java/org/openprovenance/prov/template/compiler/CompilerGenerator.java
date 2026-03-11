package org.openprovenance.prov.template.compiler;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.SpecificationFile;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generateJava;
import static org.openprovenance.prov.template.compiler.configuration.SpecificationFile.generatePython;

public class CompilerGenerator {
    private final ProvFactory provFactory;
    private final CompilerUtil compilerUtil;

    public CompilerGenerator(ProvFactory provFactory) {
        this.provFactory = provFactory;
        this.compilerUtil= new CompilerUtil(provFactory);
    }

    public SpecificationFile generateGenerator(TemplatesProjectConfiguration configs, Locations locations, String filename, String clazz, List<String> classpath, Map<String, Object> parameters) {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader extendedClassLoader = extendClassLoader(classLoader, classpath.toArray(new String[0]));
        try {
            Class<?> loadedClass = Class.forName(clazz, true, extendedClassLoader);
            GeneratorInvoker generatorInstance = (GeneratorInvoker) loadedClass.getDeclaredConstructor().newInstance();
            Pair<org.openprovenance.prov.template.compiler.past.Class, StackTraceElement> pair= generatorInstance.generate(provFactory, configs, locations, filename, parameters);
            org.openprovenance.prov.template.compiler.past.Class pastClass=pair.getLeft();
            StackTraceElement stackTraceElement=pair.getRight();

            String javaRootDirectory=locations.getCli_src_dir();
            String packageName = "org.openprovenance.book.workflows";
            javaRootDirectory = javaRootDirectory.endsWith("/") ? javaRootDirectory : javaRootDirectory + "/";

            String javaOutputDirectory= javaRootDirectory + packageName.replace(".", "/") + "/";

            Supplier<Boolean> pythonGenerator = () -> generatePython(pastClass, packageName, configs.python_dir, stackTraceElement);
            Supplier<Boolean> javaGenerator = () -> generateJava(pastClass, packageName, configs, filename + ".java", javaOutputDirectory, stackTraceElement, compilerUtil);

            SpecificationFile specFile = new SpecificationFile(javaGenerator, pythonGenerator);
            return specFile;


        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

    public static ClassLoader extendClassLoader(ClassLoader parent, String... extraDirs) {
        URL[] urls = Arrays.stream(extraDirs)
                .map(dir -> {
                    try { return Path.of(dir).toUri().toURL(); }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .toArray(URL[]::new);
        return new URLClassLoader(urls, parent);
    }

}
