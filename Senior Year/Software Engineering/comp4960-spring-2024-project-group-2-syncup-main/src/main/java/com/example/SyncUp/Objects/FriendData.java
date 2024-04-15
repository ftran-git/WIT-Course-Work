package com.example.SyncUp.Objects;

import com.inrupt.client.accessgrant.AccessRequest;

import java.net.URI;

public class FriendData {
    private URI resourceURL;
    private URI webID;

    public FriendData(URI resourceURL, URI webID) {
        this.resourceURL = resourceURL;
        this.webID = webID;
    }

    public URI getResourceURL() {
        return resourceURL;
    }

    public URI getWebID() {
        return webID;
    }

    @Override
    public String toString() {
        // Remove everything before the last "/" and replace "-" which separates last and first name with ", "
        int lastIndex = resourceURL.toString().lastIndexOf("/");
        return resourceURL.toString().substring(lastIndex + 1).replace("-", ", ");
    }
}
