package jk_5.nailed.buildscript.tasks;

import groovy.lang.Closure;
import jk_5.nailed.buildscript.Constants;
import lombok.Getter;
import lombok.Setter;
import net.md_5.specialsource.*;
import net.md_5.specialsource.provider.JarProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.process.JavaExecSpec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProcessJarTask extends CachedTask {
    @Getter
    @Setter
    @InputFile
    private File inJar;

    @Getter
    @Setter
    @InputFile
    private File exceptorJar;

    @Getter
    @Setter
    @InputFile
    private File srg;

    @Getter
    @Setter
    @InputFile
    private File exceptorCfg;

    @Getter
    @Setter
    @OutputFile
    @Cached
    private File outJar;

    @InputFiles
    private ArrayList<Object> ats = new ArrayList<Object>();

    /**
     * adds an access transformer to the deobfuscation of this
     *
     * @param obj - File or path representing the AT
     */
    public void addTransformer(Object... obj){
        this.ats.addAll(Arrays.asList(obj));
    }

    @TaskAction
    @SuppressWarnings("unused")
    public void doTask() throws IOException {
        // make stuff into files.
        File tempObfJar = new File(getTemporaryDir(), "obfed.jar"); // courtesy of gradle temp dir.

        // make the ATs LIST
        ArrayList<File> ats = new ArrayList<File>();
        for (Object obj : this.ats) {
            ats.add(getProject().file(obj));
        }

        // deobf
        getLogger().lifecycle("Applying SpecialSource...");
        deobfJar(inJar, tempObfJar, srg, ats);

        // apply exceptor
        getLogger().lifecycle("Applying Exceptor...");
        applyExceptor(exceptorJar, tempObfJar, outJar, exceptorCfg, new File(getTemporaryDir(), "exceptorLog"));
    }

    private void deobfJar(File inJar, File outJar, File srg, ArrayList<File> ats) throws IOException {
        getLogger().debug("INPUT: " + inJar);
        getLogger().debug("OUTPUT: " + outJar);
        // load mapping
        JarMapping mapping = new JarMapping();
        mapping.loadMappings(srg);

        // load in ATs
        AccessMap accessMap = new AccessMap();
        getLogger().info("Using AccessTransformers...");
        for (File at : ats) {
            getLogger().info("" + at);
            accessMap.loadAccessTransformer(at);
        }

        // make a processor out of the ATS and mappings.
        RemapperPreprocessor processor = new RemapperPreprocessor(null, mapping, accessMap);

        // make remapper
        JarRemapper remapper = new JarRemapper(processor, mapping);

        // load jar
        Jar input = Jar.init(inJar);

        // ensure that inheritance provider is used
        JointProvider inheritanceProviders = new JointProvider();
        inheritanceProviders.add(new JarProvider(input));
        mapping.setFallbackInheritanceProvider(inheritanceProviders);

        // remap jar
        remapper.remapJar(input, outJar);
    }

    public void applyExceptor(final File injectorJar, final File inJar, final File outJar, final File config, final File log) {
        getLogger().debug("INPUT: " + inJar);
        getLogger().debug("OUTPUT: " + outJar);
        getLogger().debug("CONFIG: " + config);
        // http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.JavaExec.html
        getProject().javaexec(new Closure(this) {
            public Object call() {
                JavaExecSpec exec = (JavaExecSpec) getDelegate();

                exec.args(
                        injectorJar.getAbsolutePath(),
                        inJar.getAbsolutePath(),
                        outJar.getAbsolutePath(),
                        config.getAbsolutePath(),
                        log.getAbsolutePath()
                );

                //exec.jvmArgs("-jar", injectorJar.getAbsolutePath());

                exec.setMain("-jar");
                //exec.setExecutable(injectorJar);
                exec.setWorkingDir(injectorJar.getParentFile());

                exec.classpath(Constants.getClassPath());

                exec.setStandardOutput(Constants.getNullStream());

                return exec;
            }
        });
    }
}
