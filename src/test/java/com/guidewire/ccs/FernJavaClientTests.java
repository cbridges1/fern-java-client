package com.guidewire.ccs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FernJavaClientTests {

//    private FernJavaClient fernJavaClient = new FernJavaClient();

    @BeforeAll
    static void init() {

    }

    @Test
    void report() {
        var testPath = Paths.get("test-data");
        FernJavaClient.report(testPath.toAbsolutePath().toString());
        Assertions.assertTrue(testPath.toFile().exists());
    }

    @Test
    void goodTest() {
        Assertions.assertTrue(true);
    }

    @Test
    void whackTest() {
        Assertions.assertTrue(false);
    }
}
