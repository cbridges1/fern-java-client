package com.guidewire.ccs;

import com.guidewire.ccs.domain.SuiteRun;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class FernJavaClient {
    public static void report(String reportDirectoryString) {
        File reportDirectory = new File(reportDirectoryString);
        File[] reports = reportDirectory.listFiles();

        if (reports != null) {
            for (File report : reports) {
                if (report.isFile()) {
                    try {
                        processFile(report);
                    } catch (ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SAXException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static void processFile(File testFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(testFile);
        var testXml = doc.getDocumentElement();
        System.out.println(testXml.getAttribute("name"));

//        var startTime = Instant.parse(testXml.getAttribute("timestamp"));

        var suiteRun = SuiteRun.builder().suiteName(testXml.getAttribute("name")).build();
//        testXml.getChildNodes().item(0).appendChild(testXml);
        var testCases = testXml.getElementsByTagName("testcase");
        System.out.println(suiteRun.toString());
    }
}