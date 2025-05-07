package dev.vinpol.nebula.dragonship.automation;

import dev.vinpol.spacetraders.sdk.models.Ship;
import org.mapstruct.Mapper;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(mappingControl = DeepClone.class)
public interface ShipCloner {
    ShipCloner INSTANCE = Mappers.getMapper(ShipCloner.class);

    Ship copy(Ship ship);

    static Ship clone(Ship ship) {
        if (ship == null) {
            return null;
        }

        return ShipCloner.INSTANCE.copy(ship);
    }
}
