package jk_5.nailed.buildscript;

import jk_5.nailed.buildscript.tasks.*;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.Copy;

import java.io.File;
import java.util.HashMap;

public class NailedPlugin implements Plugin<Project> {
    public Project project;

    @Override
    public void apply(Project project) {
        this.project = project;

        project.getExtensions().create(Constants.EXT_NAME, ExtensionObject.class, this);

        createDownloadTasks();
        createMappingFixTask();
        createJarProcessTasks();
        createSourceManipTasks();
        createOtherNailedTasks();
        createSourceCopyTasks();

        Task task = makeTask("setupNailed", DefaultTask.class);
        task.dependsOn("nailedPatches", "createProject");
        task.setGroup("Nailed");
    }

    private void createDownloadTasks() {
        DownloadTask task;

        task = makeTask("downloadServer", DownloadTask.class);
        task.setOutput(Constants.cacheFile(project, Constants.JAR_SERVER_FRESH));
        task.setUrl(Constants.MC_SERVER_URL);

        task = makeTask("downloadFernFlower", DownloadTask.class);
        task.setOutput(Constants.cacheFile(project, Constants.FERNFLOWER));
        task.setUrl(Constants.FERNFLOWER_URL);

        task = makeTask("downloadExceptor", DownloadTask.class);
        task.setOutput(Constants.cacheFile(project, Constants.EXCEPTOR));
        task.setUrl(Constants.EXCEPTOR_URL);
    }

    private void createMappingFixTask() {
        MergeMappingsTask task = makeTask("fixMappings", MergeMappingsTask.class);

        task.setPackageCSV(Constants.projectFile(project, Constants.PACK_CSV));
        task.setInSRG(Constants.projectFile(project, Constants.JOINED_SRG));
        task.setInEXC(Constants.projectFile(project, Constants.JOINED_EXC));
        task.setOutSRG(Constants.cacheFile(project, Constants.PACKAGED_SRG));
        task.setOutEXC(Constants.cacheFile(project, Constants.PACKAGED_EXC));
        task.setInPatch(Constants.projectFile(project, Constants.MCP_PATCH));
        task.setOutPatch(Constants.cacheFile(project, Constants.PACKAGED_PATCH));
    }

    private void createJarProcessTasks() {
        ProcessJarTask task2 = makeTask("deobfuscateJar", ProcessJarTask.class);
        task2.setInJar(Constants.cacheFile(project, Constants.JAR_SERVER_FRESH));
        task2.setExceptorJar(Constants.cacheFile(project, Constants.EXCEPTOR));
        task2.setOutJar(Constants.cacheFile(project, Constants.JAR_SRG));
        task2.setSrg(Constants.cacheFile(project, Constants.PACKAGED_SRG));
        task2.setExceptorCfg(Constants.cacheFile(project, Constants.PACKAGED_EXC));
        task2.addTransformer(Constants.projectFile(project, Constants.NAILED_RES, "nailed_at.cfg"));
        task2.dependsOn("downloadExceptor", "fixMappings", "downloadServer");

        DecompileTask task3 = makeTask("decompile", DecompileTask.class);
        task3.setInJar(Constants.cacheFile(project, Constants.JAR_SRG));
        task3.setOutZip(Constants.cacheFile(project, Constants.ZIP_DECOMP));
        task3.setFernFlower(Constants.cacheFile(project, Constants.FERNFLOWER));
        task3.setPatch(Constants.cacheFile(project, Constants.PACKAGED_PATCH));
        task3.setAstyleConfig(Constants.projectFile(project, Constants.ASTYLE_CFG));
        task3.dependsOn("downloadFernFlower", "deobfuscateJar");
    }

    private void createSourceManipTasks() {
        NailedCleanupTask cleanTask = makeTask("cleanup", NailedCleanupTask.class);
        cleanTask.setDir(new File(Constants.MINECRAFT_CLEAN_DIR));
        //cleanTask.dependsOn("copySources");

        PatchTask patch = makeTask("nailedPatches", PatchTask.class);
        patch.setPatchDir(Constants.projectFile(project, Constants.PATCH_DIR));
        patch.setFilesDir(new File(Constants.MINECRAFT_WORK_DIR));
        patch.dependsOn("copySources");
    }

    private void createSourceCopyTasks() {
        // Copy minecraft sources to CLEAN
        Copy copyTask = makeTask("extractMinecraftSources", Copy.class);
        copyTask.include("net/**");
        copyTask.from(project.zipTree(Constants.cacheFile(project, Constants.ZIP_DECOMP)));
        copyTask.into(Constants.projectFile(project, Constants.MINECRAFT_CLEAN_DIR));
        copyTask.dependsOn("decompile");

        // Copy minecraft resources to CLEAN
        copyTask = makeTask("extractMinecraftResources", Copy.class);
        copyTask.exclude("net/**");
        copyTask.from(project.zipTree(Constants.cacheFile(project, Constants.ZIP_DECOMP)));
        copyTask.into(Constants.projectFile(project, Constants.MINECRAFT_RESOURCES_DIR));
        copyTask.dependsOn("decompile");

        // Copy CLEAN to WORK
        copyTask = makeTask("copySources", Copy.class);
        copyTask.from(project.fileTree(new File(Constants.MINECRAFT_CLEAN_DIR)));
        copyTask.into(new File(Constants.MINECRAFT_WORK_DIR));
        copyTask.dependsOn("extractMinecraftSources", "extractMinecraftResources", "cleanup");
    }

    private void createOtherNailedTasks() {
        CompressLZMA compressTask = makeTask("compressDeobfData", CompressLZMA.class);
        compressTask.setInputFile(Constants.cacheFile(project, Constants.PACKAGED_SRG));
        compressTask.setOutputFile(new File("deobfuscationData.lzma"));
        compressTask.dependsOn("fixMappings");

        GeneratePatches genPatcher = makeTask("genPatches", GeneratePatches.class);
        genPatcher.setPatchDir(Constants.projectFile(project, Constants.PATCH_DIR));
        genPatcher.setOriginalDir(Constants.projectFile(project, Constants.MINECRAFT_CLEAN_DIR));
        genPatcher.setChangedDir(Constants.projectFile(project, Constants.MINECRAFT_WORK_DIR));
        genPatcher.setGroup("Nailed");

        makeTask("createProject", ProjectTask.class);

        /*Delete clean = makeTask("clean", Delete.class);
        clean.delete(Constants.projectFile(project, "minecraft"));
        clean.setGroup("Clean");*/
    }

    private <T extends Task> T makeTask(String name, Class<T> type) {
        HashMap map = new HashMap();
        map.put("name", name);
        map.put("type", type);

        return (T) project.task(map, name);
    }
}
