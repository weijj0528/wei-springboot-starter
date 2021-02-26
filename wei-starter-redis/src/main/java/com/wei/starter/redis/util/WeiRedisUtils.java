package com.wei.starter.redis.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WeiRedisUtils {
    public static String readScript(String scriptName) {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(scriptName);
            inputStreamReader = new InputStreamReader(classPathResource.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line.trim()).append("\n");
                line = bufferedReader.readLine();
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
