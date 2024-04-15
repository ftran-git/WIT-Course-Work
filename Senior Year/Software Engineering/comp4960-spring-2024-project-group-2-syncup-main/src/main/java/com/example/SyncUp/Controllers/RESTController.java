package com.example.SyncUp.Controllers;

import com.example.SyncUp.RDFResources.Container;
import com.example.SyncUp.RDFResources.Event;
import com.example.SyncUp.RDFResources.Friend;
import com.inrupt.client.accessgrant.*;
import com.inrupt.client.auth.Session;
import com.inrupt.client.openid.OpenIdSession;
import com.inrupt.client.solid.*;
import com.inrupt.client.webid.WebIdProfile;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.rdf.api.RDFSyntax;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class RESTController {
    /***
     * Inrupt Enterprise Verifiable Credential Service will be used in handling access requests and creating access grants for Inrupt's PodSpaces
     */
    private final URI PS_ACCESS_GRANT_URI = URI.create("https://vc.inrupt.com");

    /***
     * Variables to store client credentials for authentication process
     */
    private String solidIDP = "";
    private String solidClientID = "";
    private String solidClientSecret = "";
    private String authFlow = "";

    /***
     * Variable to store user's WebID and PodURL
     */
    public String solidPodURL = "";
    public String solidWebID = "";

    /***
     * Using the client credentials, create an authenticated session.
     */
    public Session session = OpenIdSession.ofClientCredentials(URI.create(solidIDP), solidClientID, solidClientSecret, authFlow);

    /***
     * Instantiates a synchronous client for the authenticated session.
     * The client has methods to perform CRUD operations.
     */
    private SolidSyncClient client = SolidSyncClient.getClient().session(session);

    /***
     * Instantiate AccessGrantClient object to handle Access Grant services
     */
    private AccessGrantClient accessGrantClient = new AccessGrantClient(PS_ACCESS_GRANT_URI).session(session);

    /***
     * Has methods for printing formatted text
     */
    private final PrintWriter printWriter = new PrintWriter(System.out, true);

    /***
     * Method to update Session and SolidSyncClient based on credentials entered by user
     * @param solidIDP
     * @param solidClientID
     * @param solidClientSecret
     * @param authFlow
     */
    public void updateState(String solidIDP, String solidClientID, String solidClientSecret, String authFlow) {
        this.session = OpenIdSession.ofClientCredentials(URI.create(solidIDP), solidClientID, solidClientSecret, authFlow);
        this.client = SolidSyncClient.getClient().session(session);
        this.accessGrantClient = new AccessGrantClient(PS_ACCESS_GRANT_URI).session(session);
    }

    /***
     * Method to set user's Solid WebID
     * @param solidWebID
     */
    public void setSolidWebID(String solidWebID) {
        this.solidWebID = solidWebID;
    }

    /***
     * Method to set user's Solid Pod URL
     * @param solidPodURL
     */
    public void setSolidPodURL(String solidPodURL) {
        this.solidPodURL = solidPodURL;
    }

    /***
     * Using the SolidSyncClient client.read() method, reads the user's WebID Profile document and returns the Pod URI(s).
     * @param webID
     * @return
     */
    @GetMapping("/pods")
    public Set<URI> getPods(@RequestParam(value = "webid", defaultValue = "") String webID) {
        printWriter.println("RESTController:: getPods");
        try (final var profile = client.read(URI.create(webID), WebIdProfile.class)) {
            return profile.getStorages();
        }
    }

    /* Controller for Event Class */

    /***
     * Using the SolidSyncClient client.create() method, saves the Event object as an RDF resource to the location specified in the Event.identifier field.
     * @param newEvent
     * @return
     */
    @PostMapping(path = "/event/create")
    public Event createEvent(@RequestBody Event newEvent) {
        printWriter.println("RESTController:: createEvent");
        try (var createdEvent = client.create(newEvent)) {
            printEventAsTurtle(createdEvent);
            return createdEvent;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.read() method, reads the RDF resource into the Event object.
     * @param resourceURL
     * @return
     */
    @GetMapping("/event/get")
    public Event getEvent(@RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        printWriter.println("RESTController:: getEvent");
        try (var resource = client.read(URI.create(resourceURL), com.example.SyncUp.RDFResources.Event.class)) {
            return resource;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.update() method, updates the Event resource.
     * @param event
     * @return
     */
    @PutMapping("/event/update")
    public Event updateEvent(@RequestBody Event event) {
        printWriter.println("RESTController:: updateEvent");

        try(var updatedEvent = client.update(event)) {
            printEventAsTurtle(updatedEvent);
            return updatedEvent;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.delete() method, deletes the Friend resource located at the resourceURL.
     * @param resourceURL
     */
    @DeleteMapping("/event/delete")
    public void deleteEvent(@RequestParam(value = "resourceURL") String resourceURL) {
        printWriter.println("RESTController:: deleteEvent");
        try {
            client.delete(URI.create(resourceURL));
            // Alternatively, you can specify an Event object to the delete method.
            // The delete method deletes the Event record located in the Expense.identifier field.
            // For example: client.delete(new Event(URI.create(resourceURL)));
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Prints the Event resource in Turtle.
     * @param event
     */
    private void printEventAsTurtle(Event event) {
        printWriter.println("RESTController:: printEventAsTurtle");
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try  {
            event.serialize(RDFSyntax.TURTLE, content);
            printWriter.println(content.toString("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Controller for Friend Class */

    /***
     * Using the SolidSyncClient client.create() method, saves the Friend object as an RDF resource to the location specified in the Event.identifier field.
     * @param newFriend
     * @return
     */
    @PostMapping(path = "/friend/create")
    public Friend createFriend(@RequestBody Friend newFriend) {
        printWriter.println("RESTController:: createFriend");
        try (var createdFriend = client.create(newFriend)) {
            printFriendAsTurtle(createdFriend);
            return createdFriend;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.read() method, reads the RDF resource into the Friend class.
     * @param resourceURL
     * @return
     */
    @GetMapping("/friend/get")
    public Friend getFriend(@RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        printWriter.println("RESTController:: getFriend");
        try (var resource = client.read(URI.create(resourceURL), com.example.SyncUp.RDFResources.Friend.class)) {
            return resource;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.update() method, updates the Friend resource.
     * @param friend
     * @return
     */
    @PutMapping("/friend/update")
    public Friend updateEvent(@RequestBody Friend friend) {
        printWriter.println("RESTController:: updateFriend");
        try(var updatedFriend = client.update(friend)) {
            printFriendAsTurtle(updatedFriend);
            return updatedFriend;
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.delete() method, deletes the Friend resource located at the resourceURL.
     * @param resourceURL
     */
    @DeleteMapping("/friend/delete")
    public void deleteFriend(@RequestParam(value = "resourceURL") String resourceURL) {
        printWriter.println("RESTController:: deleteEvent");
        try {
            client.delete(URI.create(resourceURL));
            // Alternatively, you can specify a Friend object to the delete method.
            // The delete method deletes the Friend record located in the Expense.identifier field.
            // For example: client.delete(new Friend(URI.create(resourceURL)));
        } catch (NotFoundException e1) {
            // Errors if resource is not found
            printWriter.println(String.format("[%s] com.inrupt.client.solid.NotFoundException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to read
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Prints the Friend resource in Turtle.
     * @param friend
     */
    private void printFriendAsTurtle(Friend friend) {
        printWriter.println("RESTController:: printFriendAsTurtle");
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try  {
            friend.serialize(RDFSyntax.TURTLE, content);
            printWriter.println(content.toString("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Controller for Containers */

    /***
     * Using the SolidSyncClient client.create() method, creates a new container at the path specified
     * @param newContainer
     * @return
     */
    @PostMapping(path = "/container/create")
    public Container createContainer(@RequestBody Container newContainer) {
        printWriter.println("RESTController:: createContainer");
        try (var createdContainer = client.create(newContainer)) {
            printContainerAsTurtle(createdContainer);
            return createdContainer;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Using the SolidSyncClient client.read() method, reads a pod container and returns all resources.
     * @param resourceURL
     * @return
     */
    @GetMapping("/container/get")
    public Set<URI> getContainer(@RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        printWriter.println("RESTController:: getContainer");
        try (final var container = client.read(URI.create(resourceURL), Container.class)) {
            return container.getContains();
        }
    }

    /***
     * Prints the Container resource in Turtle.
     * @param container
     */
    private void printContainerAsTurtle(Container container) {
        printWriter.println("RESTController:: printContainerAsTurtle");
        ByteArrayOutputStream content = new ByteArrayOutputStream();
        try  {
            container.serialize(RDFSyntax.TURTLE, content);
            printWriter.println(content.toString("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* Controller for Access Request and Access Grant */

    /***
     * Uses AccessGrantClient.requestAccess() to create and send an AccessRequest to a desired resource and its owner
     * @param resourceURLs
     * @param resourceOwner
     */
    @PostMapping("/accessRequest/read/create")
    public AccessRequest createAccessRequest(@RequestParam(value = "resourceURLs", defaultValue = "") List<String> resourceURLs, @RequestParam(value = "resourceOwner", defaultValue = "") String resourceOwner) {
        System.out.println("RESTController:: createAccessRequest");
        // Create URIs for resourceURLs and resourceOwner
        URI resourceOwnerURI = URI.create(resourceOwner);
        Set<URI> resourcesURIs = resourceURLs.stream().map(URI::create).collect(Collectors.toSet());

        // Define what request access modes
        Set<String> modes = Set.of("Read", "Append", "Write"); // Available modes are "Read", "Write", and "Append".

        // Define purpose for request
        Set<URI> purposes = Set.of(
                URI.create("https://purpose.example.com/RetrievingFriendsEvents"),
                URI.create("https://purpose.example.com/DisplayingFriendsEvents"));

        // Define expiration time for access if necessary
        Instant currentInstant = Instant.now();
        Instant expiration = currentInstant.plus(20, ChronoUnit.MINUTES);

        // Assemble request parameters
        AccessRequest.RequestParameters requestParams = AccessRequest.RequestParameters.newBuilder()
                .recipient(resourceOwnerURI)
                .resources(resourcesURIs)
                .modes(modes)
                .purposes(purposes)
                .expiration(expiration)
                .build();

        // Create and send AccessRequest using AccessGrantClient
        try {
            return accessGrantClient.requestAccess(requestParams).toCompletableFuture().join();
        } catch(Exception e) {
        }
        return null;
    }

    /***
     * Uses AccessGrantClient.fetch() to receive an AccessRequest from a requesting client
     */
    @GetMapping("/accessRequest/get")
    public List<AccessRequest> getAccessRequest(@RequestParam(value = "resourceOwner", defaultValue = "") String resourceOwner) {
        System.out.println("RESTController:: getAccessRequest");
        // Query and return list of AccessRequests
        return accessGrantClient.query(
                null, // Resource requested
                null, // Creator of the access request (resource requester)
                URI.create(resourceOwner),  // Recipient of the access request (resource owner)
                null,  // Purpose
                null,  // Access Mode
                AccessRequest.class).toCompletableFuture().join();
    }

    /***
     * Uses AccessGrantClient.grantAccess() to create and send an AccessGrant to a requesting client
     * @param accessRequest
     * @return
     */
    @PostMapping("/accessGrant/create")
    public AccessGrant createAccessGrant(@RequestParam(value = "accessRequest", defaultValue = "") AccessRequest accessRequest) {
        System.out.println("RESTController:: createAccessGrant");
        // Create and send AccessGrant using AccessGrantClient
        return accessGrantClient.grantAccess(accessRequest).toCompletableFuture().join();
    }

    /***
     * Uses AccessGrantClient.fetch() to receive an AccessGrant from a desired resource and its owner
     * @return
     */
    @GetMapping("/accessGrant/get")
    public AccessGrant getAccessGrant(@RequestParam(value = "grantCreator", defaultValue = "") String grantCreator, @RequestParam(value = "grantRecipient", defaultValue = "") String grantRecipient) {
        System.out.println("RESTController:: getAccessGrant");
        // Query and return first AccessGrant from list of AccessGrants (Only grabbing first element because there should only be one AccessGrant sent at a time to a user from a specified resourceOwner)
        try {
            return accessGrantClient.query(
                    null,
                    URI.create(grantCreator),       // Creator of grant (WebID); i.e., the grantor
                    URI.create(grantRecipient),       // Recipient of the access grant; i.e., the grantee
                    null,       // Purpose
                    null,     // Access Modes
                    AccessGrant.class).toCompletableFuture().join().get(0);
        } catch (Exception e) {
            return null;
        }
    }

    /***
     * Use AccessGrant to read a Friend's pod container and returns all resources
     * @param accessGrant
     */
    @GetMapping("/external/getContainer")
    public Set<URI> getContainerFromFriend(@RequestParam(value = "accessGrant", defaultValue = "") AccessGrant accessGrant, @RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        System.out.println("RESTController:: getResourceFromFriend");
        // Create an AccessGrantSession
        Session accessGrantSession = AccessGrantSession.ofAccessGrant(session, accessGrant);

        // Create a solid client
        SolidSyncClient client = SolidSyncClient.getClient().session(accessGrantSession);

        // Access the container and return all resources
        try (final var container = client.read(URI.create(resourceURL), Container.class)) {
            return container.getContains();
        }
    }

    /***
     * Using the SolidSyncClient client.create() method, creates a new container at the path specified for friend
     * @param newContainer
     * @return
     */
    @PostMapping(path = "/external/createContainer")
    public Container createContainerForFriend(@RequestParam(value = "accessGrant", defaultValue = "") AccessGrant accessGrant, @RequestParam(value = "newContainer", defaultValue = "") Container newContainer) {
        System.out.println("RESTController:: createContainerForFriend");
        // Create an AccessGrantSession
        Session accessGrantSession = AccessGrantSession.ofAccessGrant(session, accessGrant);

        // Create a solid client
        SolidSyncClient client = SolidSyncClient.getClient().session(accessGrantSession);

        try (var createdContainer = client.create(newContainer)) {
            printContainerAsTurtle(createdContainer);
            return createdContainer;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * Use AccessGrant to access a Friend's Event resource
     * @param accessGrant
     */
    @GetMapping("/external/getEvent")
    public Event getEventFromFriend(@RequestParam(value = "accessGrant", defaultValue = "") AccessGrant accessGrant, @RequestParam(value = "resourceURL", defaultValue = "") String resourceURL) {
        System.out.println("RESTController:: getResourceFromFriend");
        // Create an AccessGrantSession
        Session accessGrantSession = AccessGrantSession.ofAccessGrant(session, accessGrant);

        // Create a solid client
        SolidSyncClient client = SolidSyncClient.getClient().session(accessGrantSession);

        // Access the resource
        return client.read(URI.create(resourceURL), Event.class);
    }

    /***
     * Use AccessGrant to access a Friend's Event resource
     * @param accessGrant
     */
    public Event createEventForFriend(@RequestParam(value = "accessGrant", defaultValue = "") AccessGrant accessGrant, Event event) {
        System.out.println("RESTController:: addEventToFriend");
        // Create an AccessGrantSession
        Session accessGrantSession = AccessGrantSession.ofAccessGrant(session, accessGrant);

        // Create a solid client
        SolidSyncClient client = SolidSyncClient.getClient().session(accessGrantSession);

        try (var createdEvent = client.create(event)) {
            printEventAsTurtle(createdEvent);
            return createdEvent;
        } catch(PreconditionFailedException e1) {
            // Errors if the resource already exists
            printWriter.println(String.format("[%s] com.inrupt.client.solid.PreconditionFailedException:: %s", e1.getStatusCode(), e1.getMessage()));
        } catch(ForbiddenException e2) {
            // Errors if user does not have access to create
            printWriter.println(String.format("[%s] com.inrupt.client.solid.ForbiddenException:: %s", e2.getStatusCode(), e2.getMessage()));
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}