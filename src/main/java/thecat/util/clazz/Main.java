package thecat.util.clazz;

import javafx.util.Pair;

/**
 * Created by thecat on 10/10/16.
 */
public class Main {

    private static final String CLASS_FILE_NAME =
            "/home/thecat/workspaces/3i/DaoBeans/Report Beans/bin/com/trei/bean/report/ReportFinancial.class";

    public static void main(String[] args) {

        Pair<String, String> pair = ClazzUtils.calculatePackageAndCP(CLASS_FILE_NAME);

        System.out.println("result: " + pair);
    }

}

