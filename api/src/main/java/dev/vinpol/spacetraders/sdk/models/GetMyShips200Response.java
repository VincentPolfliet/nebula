package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetMyShips200Response implements ApiPageable<Ship> {
    private List<Ship> data = new ArrayList<>();
    private Meta meta;

    public GetMyShips200Response data(List<Ship> data) {
        this.data = data;
        return this;
    }

    public GetMyShips200Response addDataItem(Ship dataItem) {
        this.data.add(dataItem);
        return this;
    }

    public GetMyShips200Response meta(Meta meta) {
        this.meta = meta;
        return this;
    }
}

