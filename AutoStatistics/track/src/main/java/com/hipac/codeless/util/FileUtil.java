package com.hipac.codeless.util;

import com.hipac.codeless.config.Constants;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Created by youri on 2018/3/7.
 */

public class FileUtil {

    public static void writeString(String fileName,String info){
        if (StringUtil.empty(info)){
            return;
        }
        BufferedWriter writer = null;
        File file = new File(fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                //do nothing
            }
        }else {
            try {
                writer = new BufferedWriter(new FileWriter(file,true),1024);
                writer.write(info+ Constants.STR_SEPARATOR);
                writer.flush();
            } catch (IOException e) {
                //ignore
            }finally {
                if (writer != null){
                    try {

                        writer.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }
        }
    }

    public static String readString(String fileName){
        if (StringUtil.empty(fileName)){
            return null;
        }
        BufferedReader reader = null;
        File file = new File(fileName);
        if (!file.exists()){
            return null;
        }
        String readStr = "";
        String currentStr ;
        try {
            reader = new BufferedReader(new FileReader(file));
           while ((currentStr = reader.readLine()) != null){
                   readStr += currentStr;
           }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }

        return readStr;
    }

    public static void delete(File file){
        if (file == null){
            return;
        }
        if (file.exists()){
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File localFile : files) {
                     delete(localFile);
                }
            }else {
                file.delete();
            }
        }
    }


    public static void clearContent(String fileName){
        if (StringUtil.empty(fileName)){
            return;
        }
        BufferedWriter writer = null;
        File file = new File(fileName);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }else {
            try {
                writer = new BufferedWriter(new FileWriter(file),1024);
                writer.write(Constants.STR_SEPARATOR);
            } catch (IOException e) {

            }finally {
                if (writer != null){
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }
        }
    }


    public static JSONArray str2Json(String str){
        if (StringUtil.empty(str)){
            return null;
        }
        String tmp =str.substring(0,str.length()-Constants.STR_SEPARATOR.length());
        JSONArray jsonArray = null;
        try {
            String[] result = tmp.split(Constants.STR_SEPARATOR);
            if (result != null && result.length >0){
              jsonArray = new JSONArray(Arrays.asList(result));
            }
        }catch (Exception e){
            //do nothing
        }
        return jsonArray;
    }

}
