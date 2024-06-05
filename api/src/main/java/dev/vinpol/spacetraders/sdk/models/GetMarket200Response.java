package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class GetMarket200Response {
    private Market data;

    public GetMarket200Response data(Market data) {
        this.data = data;
        return this;
    }
}

