package com.github.budgerigar;

import java.io.File;
import java.util.Locale;
import com.github.paganini2008.devtools.io.Directory;
import com.github.paganini2008.devtools.io.DirectoryFilter;
import com.github.paganini2008.devtools.io.FileSearchUtils;
import com.github.paganini2008.devtools.io.FileUtils;

public class TestMain {

    public static class A {
        protected static String formatId(String id) {
            return id + " 11";
        }
    }


    public static class B extends A {
        public static String formatId(String id) {
            return id + " 22";
        }
    }

    static {
        Locale.setDefault(Locale.US);
    }

    public static void main(String[] args) {
        // System.out.println(A.formatId("123"));
        // System.out.println(String.format("%h", 1));
        // System.out.println(Locale.US.getDisplayCountry());
        //
        // System.out.println(NetUtils.getLocalHostAddress());

        File directory = new File("C:\\Users");
        File[] files = FileSearchUtils.search(directory, new DirectoryFilter() {
            @Override
            public boolean accept(Directory fileInfo) {
                if (fileInfo.getLength() > 500 * FileUtils.MB) {
                    return true;
                }
                return false;
            }
        }, 8, 5);
        for (File file : files) {
            System.out.println(file);
        }
    }

}
