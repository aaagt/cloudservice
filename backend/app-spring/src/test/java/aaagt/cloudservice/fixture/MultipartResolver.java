package aaagt.cloudservice.fixture;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class MultipartResolver {

    private static final Logger log = Logger.getLogger(MultipartResolver.class.getName());

    public static MultiValueMap<String, Resource> getMultimap(MockHttpServletResponse response) throws IOException {
        final var multiValueMap = new LinkedMultiValueMap<String, Resource>();

        String contentTypeHeader = response.getHeader(HttpHeaders.CONTENT_TYPE);
        log.info("Content type header: " + contentTypeHeader);

        String boundary = null;
        if (contentTypeHeader != null) {
            MediaType mediaType = MediaType.parseMediaType(contentTypeHeader);
            boundary = mediaType.getParameter("boundary");
        }
        log.info("Content type header, boundary parameter: " + boundary);

        MultipartStream multipartStream = new MultipartStream(
                new ByteArrayInputStream(response.getContentAsByteArray()),
                boundary.getBytes(),
                1024,
                null);

        boolean nextPart = multipartStream.skipPreamble();
        while (nextPart) {

            handlePart(multipartStream, multiValueMap);
            nextPart = multipartStream.readBoundary();

        }
        log.info("multiValueMap: " + multiValueMap);

        return multiValueMap;
    }

    private static void handlePart(MultipartStream multipartStream, MultiValueMap<String, Resource> map) throws IOException {
        Map<String, String> headers = getPartHeaders(multipartStream);
        log.info("headers: " + headers);

        var contentDispositionHeader = headers.get("content-disposition");

        ContentDisposition contentDisposition = ContentDisposition.parse(contentDispositionHeader);
        String name = contentDisposition.getName();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        // Read the headers of the current part.
        // After .readHeaders(), the stream's position will be at the start of the part's body, ready for you to read the part's actual content.
        multipartStream.readBodyData(output);

        ByteArrayResource resource = new ByteArrayResource(output.toByteArray());
        log.info(name + " resource: " + resource.getContentAsString(StandardCharsets.UTF_8));
        map.add(name, resource);
    }

    private static Map<String, String> getPartHeaders(MultipartStream multipartStream) throws IOException {
        String headers = multipartStream.readHeaders();
        BufferedReader reader = new BufferedReader(new StringReader(headers));
        Map<String, String> headerMap = new HashMap<>();
        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                line = line.trim();
                if (!StringUtils.isBlank(line) && line.contains(":")) {
                    int colon = line.indexOf(":");
                    String headerName = line.substring(0, colon).trim();
                    String headerValue = line.substring(colon + 1).trim();
                    headerMap.put(headerName.toLowerCase(), headerValue);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return headerMap;
    }

}
