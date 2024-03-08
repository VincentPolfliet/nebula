/*
 * SpaceTraders API
 * SpaceTraders is an open-universe game and learning platform that offers a set of HTTP endpoints to control a fleet of ships and explore a multiplayer universe.  The API is documented using [OpenAPI](https://github.com/SpaceTradersAPI/api-docs). You can send your first request right here in your browser to check the status of the game server.  ```json http {   \"method\": \"GET\",   \"url\": \"https://api.spacetraders.io/v2\", } ```  Unlike a traditional game, SpaceTraders does not have a first-party client or app to play the game. Instead, you can use the API to build your own client, write a script to automate your ships, or try an app built by the community.  We have a [Discord channel](https://discord.com/invite/jh6zurdWk5) where you can share your projects, ask questions, and get help from other players.
 *
 * The version of the OpenAPI document: 2.0.0
 * Contact: joel@spacetraders.io
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package dev.vinpol.spacetraders.sdk.models;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Contract details.
 */

public class Contract {
    public static final String SERIALIZED_NAME_ID = "id";

    private String id;

    public static final String SERIALIZED_NAME_FACTION_SYMBOL = "factionSymbol";

    private String factionSymbol;

    /**
     * Type of contract.
     */

    public enum TypeEnum {
        PROCUREMENT("PROCUREMENT"),

        TRANSPORT("TRANSPORT"),

        SHUTTLE("SHUTTLE");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    public static final String SERIALIZED_NAME_TYPE = "type";

    private TypeEnum type;

    public static final String SERIALIZED_NAME_TERMS = "terms";

    private ContractTerms terms;

    public static final String SERIALIZED_NAME_ACCEPTED = "accepted";

    private Boolean accepted = false;

    public static final String SERIALIZED_NAME_FULFILLED = "fulfilled";

    private Boolean fulfilled = false;

    public static final String SERIALIZED_NAME_EXPIRATION = "expiration";

    private OffsetDateTime expiration;

    public static final String SERIALIZED_NAME_DEADLINE_TO_ACCEPT = "deadlineToAccept";

    private OffsetDateTime deadlineToAccept;

    public Contract() {
    }

    public Contract id(String id) {

        this.id = id;
        return this;
    }

    /**
     * ID of the contract.
     * @return id
     **/
    

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public Contract factionSymbol(String factionSymbol) {

        this.factionSymbol = factionSymbol;
        return this;
    }

    /**
     * The symbol of the faction that this contract is for.
     * @return factionSymbol
     **/
    

    public String getFactionSymbol() {
        return factionSymbol;
    }


    public void setFactionSymbol(String factionSymbol) {
        this.factionSymbol = factionSymbol;
    }


    public Contract type(TypeEnum type) {

        this.type = type;
        return this;
    }

    /**
     * Type of contract.
     * @return type
     **/
    

    public TypeEnum getType() {
        return type;
    }


    public void setType(TypeEnum type) {
        this.type = type;
    }


    public Contract terms(ContractTerms terms) {

        this.terms = terms;
        return this;
    }

    /**
     * Get terms
     * @return terms
     **/
    

    public ContractTerms getTerms() {
        return terms;
    }


    public void setTerms(ContractTerms terms) {
        this.terms = terms;
    }


    public Contract accepted(Boolean accepted) {

        this.accepted = accepted;
        return this;
    }

    /**
     * Whether the contract has been accepted by the agent
     * @return accepted
     **/
    

    public Boolean getAccepted() {
        return accepted;
    }


    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }


    public Contract fulfilled(Boolean fulfilled) {

        this.fulfilled = fulfilled;
        return this;
    }

    /**
     * Whether the contract has been fulfilled
     * @return fulfilled
     **/
    

    public Boolean getFulfilled() {
        return fulfilled;
    }


    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }


    public Contract expiration(OffsetDateTime expiration) {

        this.expiration = expiration;
        return this;
    }

    /**
     * Deprecated in favor of deadlineToAccept
     * @return expiration
     * @deprecated
     **/
    @Deprecated
    

    public OffsetDateTime getExpiration() {
        return expiration;
    }


    public void setExpiration(OffsetDateTime expiration) {
        this.expiration = expiration;
    }


    public Contract deadlineToAccept(OffsetDateTime deadlineToAccept) {

        this.deadlineToAccept = deadlineToAccept;
        return this;
    }

    /**
     * The time at which the contract is no longer available to be accepted
     * @return deadlineToAccept
     **/
    

    public OffsetDateTime getDeadlineToAccept() {
        return deadlineToAccept;
    }


    public void setDeadlineToAccept(OffsetDateTime deadlineToAccept) {
        this.deadlineToAccept = deadlineToAccept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        return Objects.equals(this.id, contract.id) &&
               Objects.equals(this.factionSymbol, contract.factionSymbol) &&
               Objects.equals(this.type, contract.type) &&
               Objects.equals(this.terms, contract.terms) &&
               Objects.equals(this.accepted, contract.accepted) &&
               Objects.equals(this.fulfilled, contract.fulfilled) &&
               Objects.equals(this.expiration, contract.expiration) &&
               Objects.equals(this.deadlineToAccept, contract.deadlineToAccept);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, factionSymbol, type, terms, accepted, fulfilled, expiration, deadlineToAccept);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Contract {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    factionSymbol: ").append(toIndentedString(factionSymbol)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
        sb.append("    accepted: ").append(toIndentedString(accepted)).append("\n");
        sb.append("    fulfilled: ").append(toIndentedString(fulfilled)).append("\n");
        sb.append("    expiration: ").append(toIndentedString(expiration)).append("\n");
        sb.append("    deadlineToAccept: ").append(toIndentedString(deadlineToAccept)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
