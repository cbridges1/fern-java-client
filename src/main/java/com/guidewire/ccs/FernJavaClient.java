package com.guidewire.ccs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidewire.ccs.domain.SpecRun;
import com.guidewire.ccs.domain.SuiteRun;
import com.guidewire.ccs.domain.TestRun;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class FernJavaClient {
    public static void report(String reportDirectoryString) {
        File reportDirectory = new File(reportDirectoryString);
        File[] reports = reportDirectory.listFiles();

        var startTime = OffsetDateTime.now();
        ArrayList<SuiteRun> suiteRuns = new ArrayList<>();
        if (reports != null) {
            for (File report : reports) {
                if (report.isFile()) {
                    try {
                        suiteRuns.add( processFile(report));
                    } catch (ParserConfigurationException | IOException | SAXException e) {
                        throw new TestRunParserException(e.getMessage());
                    }
                }
            }
        }
        var testRun = TestRun.builder().testProjectName("TestProj").startTime(startTime).endTime(OffsetDateTime.now()).suiteRuns(suiteRuns.toArray(new SuiteRun[0])).build();
      try {
        sendTestRun(testRun, "http://localhost:8080");
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private static SuiteRun processFile(File testFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(testFile);
        doc.getDocumentElement().normalize();
        var testSuites = doc.getElementsByTagName("testsuite");
        var testXml = doc.getDocumentElement();
        var suiteStartTime = LocalDateTime.parse(testXml.getAttribute("timestamp")).atOffset(OffsetDateTime.now().getOffset());
        var startTime = suiteStartTime;

        var specRuns = new ArrayList<SpecRun>();
        if (testSuites.getLength() > 0) {
            Element suiteElement = (Element) testSuites.item(0);

            NodeList testCases = suiteElement.getElementsByTagName("testcase");
            for (int i = 0; i < testCases.getLength(); i++) {
                var testCase = testCases.item(i);
                if (testCase.getNodeType() == Node.ELEMENT_NODE) {
                    Element caseElement = (Element) testCase;
                    var testName = caseElement.getAttribute("name");
                    var time = caseElement.getAttribute("time");
                    var status = "passed";
                    var message = "";

                    if (caseElement.getElementsByTagName("failure").getLength() > 0) {
                        status = "failed";
                        Element failure = (Element) caseElement.getElementsByTagName("failure").item(0);
                        message = failure.getTextContent();
                        System.out.println("    Failure: " + message);
                    }
                    var endTime = addMilliseconds(startTime, time);
                    var specRun = SpecRun.builder().specDescription(testName).status(status).startTime(startTime).endTime(endTime).message(message).build();
                    startTime = endTime;
                    specRuns.add(specRun);
                }
            }
        }

        var name = testXml.getAttribute("name");
        var time = testXml.getAttribute("time");

        var endTime = addMilliseconds(suiteStartTime, time);
        var suiteRun = SuiteRun.builder().suiteName(name).startTime(startTime).endTime(endTime).specRuns(specRuns.toArray(SpecRun[]::new)).build();
        return suiteRun;
    }

    public static void sendTestRun(TestRun testRun, String serviceUrl) throws TestRunParserException, IOException {
        if (testRun == null || testRun.toString().isEmpty()) {
            return;
        }
        var mapper = new ObjectMapper();
        var jsonPayload = "";
        try {
            jsonPayload = mapper.writeValueAsString(testRun);
            var connection = getHttpURLConnection(serviceUrl + "/api/testrun", jsonPayload);

            var responseCode = connection.getResponseCode();
            if (responseCode >= 300) {
                throw new TestRunParserException("Unexpected response code: " + responseCode);
            }

            connection.disconnect();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static OffsetDateTime addMilliseconds(OffsetDateTime dateTime, String milliseconds) {
        return dateTime.plus((long) (Float.parseFloat(milliseconds) * 100L), ChronoUnit.MILLIS);
    }

    private static HttpURLConnection getHttpURLConnection(String endpointUrl, String jsonPayload) throws IOException {
        var url = new URL(endpointUrl);
        var connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }

    private static HttpURLConnection getHttpURLConnection(String endpointUrl) throws IOException {
        return getHttpURLConnection(endpointUrl, "");
    }
}