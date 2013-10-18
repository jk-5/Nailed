package jk_5.nailed.buildscript.tasks;

import com.google.common.base.Joiner;
import com.google.common.io.Files;
import groovy.lang.Closure;
import jk_5.nailed.buildscript.Constants;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class offers some extra helper methods for caching files.
 */
public abstract class CachedTask extends DefaultTask {
    private final ArrayList<Annotated> cachedList = new ArrayList<Annotated>();

    private final ArrayList<Annotated> inputList = new ArrayList<Annotated>();

    public CachedTask() {
        super();

        Class clazz = this.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (f.isAnnotationPresent(Cached.class)) {
                    addCachedField(new Annotated(clazz, f.getName()));
                }

                if (f.isAnnotationPresent(InputFile.class) || f.isAnnotationPresent(Input.class)) {
                    inputList.add(new Annotated(clazz, f.getName()));
                }
            }

            clazz = clazz.getSuperclass();
        }

        this.onlyIf(new Closure(this, this) {
            @Override
            public Object call() {
                for (Annotated field : cachedList) {

                    try {
                        File file = getFile(field);

                        // not there? do the task.
                        if (!file.exists()) {
                            return true;
                        }

                        String foundMD5 = Files.toString(getHashFile(file), Charset.defaultCharset());
                        String calcMD5 = getHashes(field, inputList, getDelegate());

                        getProject().getLogger().info("Cached file found: " + file);
                        getProject().getLogger().info("Checksums found: " + foundMD5);
                        getProject().getLogger().info("Checksums calculated: " + calcMD5);

                        if (!calcMD5.equals(foundMD5)) {
                            getProject().getLogger().error("Corrupted Cache!");
                            file.delete();
                            getHashFile(file).delete();
                            return true;
                        }

                    }
                    // error? spit it and do the task.
                    catch (Exception e) {
                        e.printStackTrace();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Object call(Object obj) {
                return call();
            }

            private File getFile(Annotated field) throws IllegalAccessException, NoSuchFieldException {
                Field f = field.taskClass.getDeclaredField(field.fieldName);
                f.setAccessible(true);
                return getProject().file(f.get(getDelegate()));
            }
        });
    }

    private void addCachedField(final Annotated annot) {
        cachedList.add(annot);

        this.doLast(new Closure(this, this) {

            @Override
            public Object call() {
                try {
                    File hashFile = getHashFile(getProject().file(annot.getValue(getDelegate())));
                    Files.write(getHashes(annot, inputList, getDelegate()), hashFile, Charset.defaultCharset());
                }
                // error? spit it and do the task.
                catch (Exception e) {
                    e.printStackTrace();
                }

                return true;
            }

            @Override
            public Object call(Object obj) {
                return call();
            }
        });
    }

    private File getHashFile(File file) {
        return new File(file.getParentFile(), file.getName() + ".md5");
    }

    private String getHashes(Annotated output, List<Annotated> inputs, Object instance) throws NoSuchFieldException, IllegalAccessException, NoSuchAlgorithmException {
        ArrayList<String> hashes = new ArrayList<String>();

        hashes.add(Constants.hash(getProject().file(output.getValue(instance))));

        for (Annotated input : inputs) {
            Field f = input.getField();

            if (f.isAnnotationPresent(InputFile.class)) {
                hashes.add(Constants.hash(getProject().file(input.getValue(instance))));
            } else {
                Object obj = input.getValue(instance);

                if (obj instanceof Closure)
                    obj = ((Closure) obj).call();

                hashes.add(Constants.hash((String) obj));
            }
        }

        return Joiner.on(Constants.NEWLINE).join(hashes);
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Cached {
    }

    private class Annotated {
        private final Class taskClass;
        private final String fieldName;

        private Annotated(Class taskClass, String fieldName) {
            this.taskClass = taskClass;
            this.fieldName = fieldName;
        }

        protected Field getField() throws NoSuchFieldException {
            return taskClass.getDeclaredField(fieldName);
        }

        protected Object getValue(Object instance) throws NoSuchFieldException, IllegalAccessException {
            Field f = getField();
            f.setAccessible(true);
            return f.get(instance);
        }
    }
}
