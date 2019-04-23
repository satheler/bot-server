package br.com.satheler.bot.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ReflectionHelper {

    /**
     * Método para encontrar classes implementadas a partir do nome do pacote informado.
     * @param <T> (GENÉRICO *DO TIPO T*).
     * @param referrerClass Recebe referencia de uma classe.
     * @param fromPackageName Recebe o nome do pacote para a verificação das classes.
     * @return Uma lista de classes do tipo de uma referência.
     */
    public static <T> List<Class<T>> findClassesImplementing(final Class<T> referrerClass, final String fromPackageName) {
        if (referrerClass == null) {
            return null;
        }

        final List<Class<T>> rVal = new ArrayList<Class<T>>();
        try {
            final Class<T>[] targets = getAllClassesFromPackage(fromPackageName);
            if (targets != null) {
                for (Class<T> aTarget : targets) {
                    if (aTarget == null) {
                        continue;
                    } else if (aTarget.equals(referrerClass)) {
                        continue;
                    } else if (!referrerClass.isAssignableFrom(aTarget)) {
                        continue;
                    } else {
                        rVal.add(aTarget);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error reading package name.");
        } catch (IOException e) {
            System.err.println("Error reading classes in package.");
        }

        return rVal;
    }

    /**
     * Método para encontrar classes implementadas a partir da referencia de uma classe.
     * @param <T> (GENÉRICO *DO TIPO T*).
     * @param referrerClass Recebe referencia de uma classe.
     * @return Uma lista de classes do tipo de uma classe referenciada.
     */
    public static <T> List<Class<T>> findClassesImplementing(final Class<T> referrerClass) {
        return findClassesImplementing(referrerClass, referrerClass.getPackage().getName());
    }

    /**
     * Método para encontrar classes implementadas a partir do nome do pacote informado.
     * @param <T> (GENÉRICO *DO TIPO T*).
     * @param referrerClass Recebe referencia de uma classe.
     * @param fromPackageName Recebe o nome do pacote para a verificação das classes.
     * @return Uma lista de classes do tipo de uma referência.
     */
    public static <T> List<Class<T>> findClassesImplementing(final Class<T> referrerClass, final Package fromPackage) {
        return findClassesImplementing(referrerClass, fromPackage.getName());
    }


    /**
     * Método para carregar todas as classes de um pacote informado.
     * @param packageName Recebe nome do pacote.
     * @return Vetor de classes que estão dentro do pacote informado.
     * @throws ClassNotFoundException É uma exceção verificada que ocorre quando um aplicativo tenta
     *         carregar uma classe por meio de seu nome completo e não pode encontrar sua definição no 
     *         caminho de classe.
     * @throws IOException Sinaliza que ocorreu uma exceção de I/O de algum tipo de falha ou interrupção.
     */
    public static Class[] getAllClassesFromPackage(final String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Método para encontrar arquivos de um pacote.
     * @param <T> (GENÉRICO *DO TIPO T*).
     * @param directory Recebe nome do diretório.
     * @param packageName Recebe nome do pacote.
     * @return Lista com classes encontradas.
     * @throws ClassNotFoundException É uma exceção verificada que ocorre quando um aplicativo tenta
     *         carregar uma classe por meio de seu nome completo e não pode encontrar sua definição no 
     *         caminho de classe.
     */
    public static <T> List<Class<T>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<T>> classes = new ArrayList<Class<T>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add((Class<T>) Class
                        .forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}

//"D:\University\Semesters\5%20Semester\RSD\RSD-T1\server\target\classes\br\com\satheler\bot\models"
