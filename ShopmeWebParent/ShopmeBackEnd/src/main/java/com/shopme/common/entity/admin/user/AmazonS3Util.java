package com.shopme.common.entity.admin.user;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class AmazonS3Util {
    public static final String BUCKET_NAME;
    private static final Logger LOGGER = LoggerFactory.getLogger(AmazonS3Util.class);

    static {
        BUCKET_NAME = System.getenv("AWS_BUCKET_NAME");
    }

    public static List<String> listFolder(String folderName) {
        S3Client client = S3Client.builder().build();
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME).prefix(folderName).build();

        ListObjectsResponse response = client.listObjects(listRequest);

        List<S3Object> contents = response.contents();

        ListIterator<S3Object> listIterator = contents.listIterator();

        List<String> listKey = new ArrayList<>();

        while (listIterator.hasNext()) {
            S3Object object = listIterator.next();
            listKey.add(object.key());
        }

        return listKey;
    }


    public static void uploadFile(String folderName, String fileName, InputStream inputStream) {
        S3Client client = S3Client.builder().build();

        PutObjectRequest request = PutObjectRequest.builder().bucket(BUCKET_NAME).key(folderName + "/" + fileName)
                .acl("public-read").build();

        try (inputStream) {
            int contentLength = inputStream.available();
            client.putObject(request, RequestBody.fromInputStream(inputStream, contentLength));
        } catch (IOException e) {
            LOGGER.error("Could not upload file to Amazon S3", e);
        }
    }

    public static void deleteFile(String fileName) {
        S3Client client = S3Client.builder().build();
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(fileName).build();

        client.deleteObject(deleteRequest);
    }

    public static void removeFolder(String folderName){


        S3Client client = S3Client.builder().build();
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucket(BUCKET_NAME).prefix(folderName).build();

        ListObjectsResponse response = client.listObjects(listRequest);

        List<S3Object> contents = response.contents();

        ListIterator<S3Object> listIterator = contents.listIterator();


        while (listIterator.hasNext()) {
            String key = listIterator.next().key();
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(key).build();
            client.deleteObject(deleteRequest);
        }

    }

}
