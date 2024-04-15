package com.example.SyncUp.Objects;

import com.inrupt.client.accessgrant.AccessRequest;

import java.net.URI;

public class AccessRequestData {
    private URI uri;
    private AccessRequest accessRequest;

    public AccessRequestData(URI uri, AccessRequest accessRequest) {
        this.uri = uri;
        this.accessRequest = accessRequest;
    }

    public URI getUri() {
        return uri;
    }

    public AccessRequest getAccessRequest() {
        return accessRequest;
    }

    @Override
    public String toString() {
        return uri.toString(); // Display URI string representation
    }
}
