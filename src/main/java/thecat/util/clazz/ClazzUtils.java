package thecat.util.clazz;

import javafx.util.Pair;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thecat on 09/10/16.
 */
public class ClazzUtils {

    /**
     * Calculate the full qualified class name (with package) and the class path of
     * the specified class name file
     *
     * @param classFile Java compiled .class file name
     * @return A pair with class path and full qualified class name
     */
    public static Pair<String, String> calculatePackageAndCP(String classFile) {

        String[] tokens = classFile.split("/");

        StringBuilder sbClazzName = new StringBuilder();
        StringBuilder sbClassPath = new StringBuilder();

        for (int i=tokens.length-1; i>0; i--) {

            if (i == tokens.length-1) {
                sbClazzName.insert(0, tokens[i].split("[.]")[0]);
            } else {
                sbClazzName.insert(0, '.').insert(0, tokens[i]);
            }

            sbClassPath.setLength(0);
            for (int j=0; j<i; j++) {
                sbClassPath.append(tokens[j]).append('/');
            }

            System.out.print("trying with cp " + sbClassPath.toString() + " and class " + sbClazzName.toString() + "...");
            try {
                Class clazz = classForName(sbClazzName.toString(), new String[]{sbClassPath.toString()});
                System.out.println("found class");
                return new Pair<>(sbClassPath.toString(), sbClazzName.toString());
            } catch (ClassNotFoundException cnfe) {
                System.out.println("class not found");
            } catch (NoClassDefFoundError ncdfe) {
                System.out.println("class not found");
            }
        }

        return null;
    }

    private static Class classForName(String className, String[] additionalClassPath) throws ClassNotFoundException, NoClassDefFoundError {
        Class clazz;

        // load the class using a custom class loader
        if (additionalClassPath.length > 0) {
            URL[] urls = new URL[additionalClassPath.length];
            int i=0;
            for (String path : additionalClassPath) {
                try {
                    urls[i++] = new File(path).toURI().toURL();
                } catch (MalformedURLException ex) { }
            }
            URLClassLoader urlClassLoader = new URLClassLoader(urls);

            clazz = Class.forName(className, true, urlClassLoader);
        }
        // use the default class loader
        else {
            clazz = Class.forName(className);
        }

        return clazz;
    }

    public static List<Method> getClassMethods(String className, boolean includeSuper, String[] additionalClassPath) throws ClassNotFoundException, NoClassDefFoundError {
        //System.out.println("Searching for methods on " + className);

        Class clazz = classForName(className, additionalClassPath);
        Class superClazz = null;

        if (includeSuper) {
            superClazz = clazz.getSuperclass();
        }

        List<Method> methods = getClassGetMethods(clazz);

        if (includeSuper && !superClazz.getCanonicalName().equals("java.lang.Object")) {
            List<Method> superClazzMethods = getClassGetMethods(superClazz);
            methods.addAll(superClazzMethods);
        }

        //System.out.println("found " + methods.size() + " methods");
        return methods;
    }

    private static List<Method> getClassGetMethods(Class clazz) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().startsWith("get")) {
                methods.add(method);

                //System.out.println("found method: " + method.getName());
            }
        }

        return methods;
    }
}
