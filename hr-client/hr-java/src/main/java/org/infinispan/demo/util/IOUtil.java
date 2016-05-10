package org.infinispan.demo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class IOUtil {

    public static String getScript(String scriptPath) throws IOException {

        String script = "";
        try (InputStream is = IOUtil.class.getClassLoader().getResourceAsStream(scriptPath)) {
            Scanner s = new Scanner(is).useDelimiter("\\A");
            script = s.hasNext() ? s.next() : null;
        }
        return script;
    }

}
