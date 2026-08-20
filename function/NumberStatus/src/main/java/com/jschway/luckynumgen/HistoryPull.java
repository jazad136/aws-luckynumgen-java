package com.jschway.luckynumgen;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 *
 * @author JonathanSaddler
 */
public class HistoryPull {
    
    
    public static String getFromS3(S3Client s3Client, String bucketName, String bucketKey)  {
        try (ResponseInputStream<GetObjectResponse> body = s3Client.getObject(b -> b.bucket(bucketName).key(bucketKey));) { 
            String toReturn = new String(body.readAllBytes(), StandardCharsets.UTF_8);
            body.abort();
            return toReturn;
        } catch(IOException iex) { 
            return iex.getMessage();
        } catch(S3Exception | SdkClientException e) {
            return e.getMessage();
        }
    }
    public static String uploadToS3(S3Client s3Client, String bucketName, String bucketKey, String content) {
        try {
            RequestBody body = RequestBody.fromString(content);
            s3Client.putObject(b -> b.bucket(bucketName).key(bucketKey), body);
            return "";
        } catch(S3Exception | SdkClientException e) {
            return e.getMessage();
        }
    }
}
