package org.ektorp.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ektorp.DocumentOperationResult;
import org.ektorp.http.RestTemplate;
import org.ektorp.http.URI;

import java.io.InputStream;
import java.util.List;

public class DefaultBulkStreamExecutor implements BulkStreamExecutor {

    protected URI dbURI;

    protected RestTemplate restTemplate;

    protected ObjectMapper objectMapper;

    protected BulkDocumentWriter writer;

    protected String bulkDocsUri;

    public DefaultBulkStreamExecutor(URI dbURI, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.dbURI = dbURI;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.writer = new BulkDocumentWriter(objectMapper);
        this.bulkDocsUri = dbURI.append("_bulk_docs").toString();
    }

    @Override
    public List<DocumentOperationResult> executeBulk(InputStream inputStream, boolean allOrNothing) {
        return restTemplate.post(
                bulkDocsUri,
                writer.createInputStreamWrapper(allOrNothing, inputStream),
                new BulkOperationResponseHandler(objectMapper));

    }
}
