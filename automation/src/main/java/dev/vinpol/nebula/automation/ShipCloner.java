package dev.vinpol.nebula.automation;

import dev.vinpol.spacetraders.sdk.models.Ship;
import dev.vinpol.spacetraders.sdk.models.ShipNav;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

@Mapper(mappingControl = DeepClone.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
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
