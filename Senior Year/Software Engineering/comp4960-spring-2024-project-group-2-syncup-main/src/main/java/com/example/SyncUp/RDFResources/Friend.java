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

/**
 * Part 1
 * Note: extends SolidRDFSource
 * To model the Friend class as an RDF resource, the Friend class extends SolidRDFSource.
 * <p>
 * The @JsonIgnoreProperties annotation is added to ignore non-class-member fields
 * when serializing Expense data as JSON.
 */
@JsonIgnoreProperties(value = { "metadata", "headers", "graph", "graphNames", "entity", "contentType" })
public class Friend extends SolidRDFSource {

    /**
     * Note 2a: Predicate Definitions
     * The following constants define the Predicates used in our triple statements.
     */
    static IRI RDF_TYPE = rdf.createIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
    static IRI SCHEMA_ORG_GIVEN_NAME = rdf.createIRI("https://schema.org/givenName");
    static IRI SCHEMA_ORG_FAMILY_NAME = rdf.createIRI("https://schema.org/familyName");
    static IRI SCHEMA_ORG_IDENTIFIER = rdf.createIRI("https://schema.org/identifier");

    /**
     * Note 2b: Value Definition
     * The following constant define the value for the predicate RDF_TYPE.
     */
    static URI MY_RDF_TYPE_VALUE = URI.create("https://schema.org/Person");

    /**
     * Note 3: Node class
     * The Node class is an inner class (defined below) that handles the mapping between friend data and RDF triples.
     * The subject contains the friend data.
     */
    private final Node subject;

    /**
     * Note 4: Constructors
     * Friend constructors to handle SolidResource fields:
     * - identifier: The destination URI of the resource;
     * - dataset: The org.apache.commons.rdf.api.Dataset that corresponding to the resource.
     * - headers:  The com.inrupt.client.Headers that contains HTTP header information.
     * <p>
     * In addition, the subject field is initialized.
     */

    public Friend(final URI identifier, final Dataset dataset, final Headers headers) {
        super(identifier, dataset, headers);
        this.subject = new Node(rdf.createIRI(identifier.toString()), getGraph());
    }

    public Friend(final URI identifier) {
        this(identifier, null, null);
    }

    @JsonCreator
    public Friend(@JsonProperty("identifier") final URI identifier,
                  @JsonProperty("firstName") String firstName,
                  @JsonProperty("lastName") String lastName,
                  @JsonProperty("webID") String webID) {
        this(identifier);
        this.setRDFType(MY_RDF_TYPE_VALUE);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setWebID(webID);
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

    public String getFirstName() {
        return subject.getFirstName();
    }

    public void setFirstName(String firstName) {
        subject.setFirstName(firstName);
    }

    public String getLastName() {
        return subject.getLastName();
    }

    public void setLastName(String lastName) {
        subject.setLastName(lastName);
    }

    public String getWebID() {
        return subject.getWebID();
    }

    public void setWebID(String webID) {
        subject.setWebID(webID);
    }

    /**
     * Note 6: Inner class ``Node`` that extends WrapperIRI
     * Node class handles the mapping of the friend data to RDF triples
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

        String getFirstName() {
            return anyOrNull(SCHEMA_ORG_GIVEN_NAME, ValueMappings::literalAsString);
        }

        void setFirstName(String firstName) {
            overwriteNullable(SCHEMA_ORG_GIVEN_NAME, firstName, TermMappings::asStringLiteral);
        }

        String getLastName() {
            return anyOrNull(SCHEMA_ORG_FAMILY_NAME, ValueMappings::literalAsString);
        }

        void setLastName(String lastName) {
            overwriteNullable(SCHEMA_ORG_FAMILY_NAME, lastName, TermMappings::asStringLiteral);
        }

        String getWebID() {
            return anyOrNull(SCHEMA_ORG_IDENTIFIER, ValueMappings::literalAsString);
        }

        void setWebID(String webID) {
            overwriteNullable(SCHEMA_ORG_IDENTIFIER, webID, TermMappings::asStringLiteral);
        }
    }
}
