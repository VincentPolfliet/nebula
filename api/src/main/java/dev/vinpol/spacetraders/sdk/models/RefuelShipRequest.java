package dev.vinpol.spacetraders.sdk.models;

import lombok.Data;

@Data
public class RefuelShipRequest {
	private Integer units;
	private boolean fromCargo;

	public RefuelShipRequest units(Integer units) {
		this.units = units;
		return this;
	}

	public RefuelShipRequest fromCargo(boolean fromCargo) {
		this.fromCargo = fromCargo;
		return this;
	}
}

