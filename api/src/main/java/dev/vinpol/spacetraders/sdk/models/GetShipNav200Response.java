package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class GetShipNav200Response {
    private GetShipNav200ResponseData data;

    public GetShipNav200Response data(GetShipNav200ResponseData data) {
        this.data = data;
        return this;
    }
}

