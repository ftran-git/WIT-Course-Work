package com.example.SyncUp.RDFResources;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inrupt.client.Headers;
import com.inrupt.client.solid.SolidRDFSource;
import com.inrupt.rdf.wrapping.commons.TermMappings;
import com.inrupt.rdf.wrapping.commons.ValueMappings;
import com.inrupt.rdf.wrapping.commons.WrapperIRI;
import org.apache.commons.rdf.api.Dataset;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.api.RDFTerm;
import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.rdf.api.Literal;

/**
 * Part 1
 * Note: extends SolidRDFSource
 * To model the Event class as an RDF resource, the Event class extends SolidRDFSource.
 * <p>
 * The @JsonIgnoreProperties annotation is added to ignore non-class-member fields
 * when serializing Expense data as JSON.
 */
@JsonIgnoreProperties(value = { "metadata", "headers", "graph", "graphNames", "entity", "contentType" })
public class Event extends SolidRDFSource {

    /**
     * Note 2a: Predicate Definitions
     * The following constants define the Predicates used in our triple statements.
     */
    static IRI RDF_TYPE = rdf.createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    static IRI SCHEMA_ORG_START_DATE = rdf.createIRI("https://schema.org/startDate");
    static IRI SCHEMA_ORG_END_DATE = rdf.createIRI("https://schema.org/endDate");
    static IRI SCHEMA_ORG_LOCATION = rdf.createIRI("https://schema.org/location");
    static IRI SCHEMA_ORG_DESCRIPTION = rdf.createIRI("https://schema.org/description");
    static IRI SCHEMA_ORG_ATTENDEE = rdf.createIRI("https://schema.org/attendee");

    /**
     * Note 2b: Value Definition
     * The following constant define the value for the predicate RDF_TYPE.
     */
    static URI MY_RDF_TYPE_VALUE = URI.create("https://schema.org/Event");

    /**
     * Note 3: Node class
     * The Node class is an inner class (defined below) that handles the mapping between event data and RDF triples.
     * The subject contains the expense data.
     */
    private final Node subject;

    /**
     * Note 4: Constructors
     * Expense constructors to handle SolidResource fields:
     * - identifier: The destination URI of the resource;
     * - dataset: The org.apache.commons.rdf.api.Dataset that corresponding to the resource.
     * - headers:  The com.inrupt.client.Headers that contains HTTP header information.
     * <p>
     * In addition, the subject field is initialized.
     */

    public Event(final URI identifier, final Dataset dataset, final Headers headers) {
        super(identifier, dataset, headers);
        this.subject = new Node(rdf.createIRI(identifier.toString()), getGraph());
    }

    public Event(final URI identifier) {
        this(identifier, null, null);
    }

    @JsonCreator
    public Event(@JsonProperty("identifier") final URI identifier,
                 @JsonProperty("startDate") Date startDate,
                 @JsonProperty("endDate") Date endDate,
                 @JsonProperty("location") String location,
                 @JsonProperty("description") String description,
                 @JsonProperty("attendees") List<String> attendees) {
        this(identifier);
        this.setRDFType(MY_RDF_TYPE_VALUE);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setLocation(location);
        this.setDescription(description);
        this.setAttendees(attendees);
    }

    /**
     * Note 5: Various getters/setters.
     * The getters and setters reference the subject's methods.
     */

    public URI getRDFType() {
        return subject.getRDFType();
    }

    public void setRDFType(URI rdfType) {
        subject.setRDFType(rdfType);
    }

    public Date getStartDate() {
        return subject.getStartDate();
    }

    public void setStartDate(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR_OF_DAY, 4);

        Date adjustedStartDate = calendar.getTime();
        subject.setStartDate(adjustedStartDate);
    }

    public Date getEndDate() {
        return subject.getEndDate();
    }

    public void setEndDate(Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR_OF_DAY, 4);

        Date adjustedEndDate = calendar.getTime();
        subject.setEndDate(adjustedEndDate);
    }

    public String getLocation() {
        return subject.getLocation();
    }

    public void setLocation(String location) {
        subject.setLocation(location);
    }

    public String getDescription() {
        return subject.getDescription();
    }

    public void setDescription(String description) {
        subject.setDescription(description);
    }

    public List<String> getAttendees() {
        return subject.getAttendees();
    }

    public void setAttendees(List<String> attendees) {
        subject.setAttendees(attendees);
    }

    /**
     * Note 6: Inner class ``Node`` that extends WrapperIRI
     * Node class handles the mapping of the event data to RDF triples
     * <subject> <predicate> <object>.
     * <p>
     * Nomenclature Background: A set of RDF triples is called a Graph.
     */
    class Node extends WrapperIRI {

        Node(final RDFTerm original, final Graph graph) {
            super(original, graph);
        }

        URI getRDFType() {
            return anyOrNull(RDF_TYPE, ValueMappings::iriAsUri);
        }

        /**
         * Note 7: In its getters, the ``Node`` class calls WrapperBlankNodeOrIRI
         * method ``anyOrNull`` to return either 0 or 1 value mapped to the predicate.
         * You can use ValueMappings method to convert the value to a specified type.
         * <p>
         * In its setters, the ``Node`` class calls WrapperBlankNodeOrIRI
         * method ``overwriteNullable`` to return either 0 or 1 value mapped to the predicate.
         * You can use TermMappings method to store the value with the specified type information.
         */

        void setRDFType(URI type) {
            overwriteNullable(RDF_TYPE, type, TermMappings::asIri);
        }

        Date getStartDate() {
            Instant startInstant = anyOrNull(SCHEMA_ORG_START_DATE, ValueMappings::literalAsInstant);
            return startInstant != null ? Date.from(startInstant) : null;
        }

        void setStartDate(Date startDate) {
            Instant startInstant = startDate.toInstant();
            overwriteNullable(SCHEMA_ORG_START_DATE, startInstant, TermMappings::asTypedLiteral);
        }

        Date getEndDate() {
            Instant endInstant = anyOrNull(SCHEMA_ORG_END_DATE, ValueMappings::literalAsInstant);
            return endInstant != null ? Date.from(endInstant) : null;
        }

        void setEndDate(Date endDate) {
            Instant endInstant = endDate.toInstant();
            overwriteNullable(SCHEMA_ORG_END_DATE, endInstant, TermMappings::asTypedLiteral);
        }

        String getLocation() {
            return anyOrNull(SCHEMA_ORG_LOCATION, ValueMappings::literalAsString);
        }

        void setLocation(String location) {
            overwriteNullable(SCHEMA_ORG_LOCATION, location, TermMappings::asStringLiteral);
        }

        String getDescription() {
            return anyOrNull(SCHEMA_ORG_DESCRIPTION, ValueMappings::literalAsString);
        }

        void setDescription(String description) {
            overwriteNullable(SCHEMA_ORG_DESCRIPTION, description, TermMappings::asStringLiteral);
        }

        List<String> getAttendees() {
            List<String> attendees = new ArrayList<>();
            getGraph().stream(this, SCHEMA_ORG_ATTENDEE, null).forEach(triple -> {
                if (triple.getObject() instanceof Literal) {
                    Literal literal = (Literal) triple.getObject();
                    attendees.add(literal.getLexicalForm());
                }
            });
            return attendees;
        }

        void setAttendees(List<String> attendees){
            overwriteNullable(SCHEMA_ORG_ATTENDEE, attendees, TermMappings::asStringLiteral);
        }
    }
}