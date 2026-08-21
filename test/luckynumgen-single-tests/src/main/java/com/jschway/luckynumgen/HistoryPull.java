package com.jschway.luckynumgen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 *
 * @author JonathanSaddler
 */
public class HistoryPull {
    
    
    public static String getFromS3(S3Client s3Client, String bucketName, String bucketKey)  {
        File[] dirAndFile = getDirAndFile(bucketKey);
        File dir = dirAndFile[0];
        File file = dirAndFile[1];
        try(FileInputStream body = new FileInputStream(new File(dir, file.getName()));) { 
            String toReturn = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            return toReturn;
        } catch(IOException iex) {
            return "Error: " + iex.getMessage();
        } 
    }
    public static File[] getDirAndFile(String bucketKey) { 
        File tempS3Dir = Paths.get("src","test","resources","testS3").toFile();
        String[] keyStrs = bucketKey.split("/", 1);
        String innerDirStr = keyStrs[0];
        String innerFilenamePart = keyStrs[1];
        File innerDir = new File(tempS3Dir, innerDirStr);
        return new File[]{innerDir, new File(innerDir, innerFilenamePart)};
    }
    public static String uploadToS3(S3Client s3Client, String bucketName, String bucketKey, String content) {
        File[] dirAndFile = getDirAndFile(bucketKey);
        File dir = dirAndFile[0];
        File file = dirAndFile[1];
        dir.mkdirs();
        try (FileWriter fw = new FileWriter(new File(dir, file.getName()));) {
            fw.write(content);
            fw.flush();
            return "";
//            RequestBody body = RequestBody.fromString(content);
//            s3Client.putObject(b -> b.bucket(bucketName).key(bucketKey), body);
//            return "";
        } catch(IOException e) { 
            return e.getMessage();
        } 
    }
}
