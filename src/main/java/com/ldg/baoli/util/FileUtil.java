package com.ldg.baoli.util;

import java.io.*;
import java.net.InetAddress;

/**
 *
 */
public class FileUtil {

    public static void main(String[] args) throws Exception{
        System.err.println(InetAddress.getLocalHost().getHostName());
        System.err.println(InetAddress.getLocalHost().getCanonicalHostName());
        System.err.println(InetAddress.getLocalHost().getHostAddress());


    }

    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
