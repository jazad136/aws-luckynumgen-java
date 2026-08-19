package com.jschway.luckynumgen;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 *
 * @author jsaddle
 */
public class Setup {
    
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
    public static S3Client s3Client() { 
        return S3Client.builder()
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create("https://s3.us-east-1.amazonaws.com"))
            .forcePathStyle(true)
            .build();
    }
    public static APIGatewayProxyResponseEvent response() {
        return new APIGatewayProxyResponseEvent()
                    .withHeaders(headers())
                    .withMultiValueHeaders(multiValueHeaders());
    }
    public static Map<String, String> headers() { 
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        return headers;
    }
    public static Map<String, List<String>> multiValueHeaders() { 
        return Map.of(
            "Access-Control-Allow-Origin",List.of("*")
        );
    }
}
