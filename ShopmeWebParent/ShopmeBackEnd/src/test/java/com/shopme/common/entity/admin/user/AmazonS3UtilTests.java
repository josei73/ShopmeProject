package com.shopme.common.entity.admin.user;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AmazonS3UtilTests {

    @Test
    public void testListFolder() {
        String folder = "test-upload";
        List<String> keys = AmazonS3Util.listFolder(folder);

        keys.forEach(System.out::println);

    }

    @Test
    public void testUploadFile() throws FileNotFoundException {

        String folder = "product-images/one/two/three";
        String fielName = "tengen.jpeg";
        String filePath = "C:\\Users\\black\\OneDrive\\Bilder\\Eigene Aufnahmen\\" + fielName;

        InputStream inputStream = new FileInputStream(filePath);

        AmazonS3Util.uploadFile(folder, fielName, inputStream);

    }

    @Test
    public void testDeleteFile(){
        String fieleName = "product-images/one/two/three/tengen.jpeg";
        AmazonS3Util.deleteFile(fieleName);
    }


    @Test
    public void testRemoveFolder(){
        String folder = "product-images/one/two/three";

        AmazonS3Util.removeFolder(folder);
    }

}
