package dev.vinpol.nebula.dragonship.web.galaxy;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.control.DeepClone;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface MapDataCloner {
    MapDataCloner INSTANCE = Mappers.getMapper(MapDataCloner.class);

    @Mapping(target = "types", ignore = true)
    @Mapping(target = "waypoints", source = "waypoints", qualifiedByName = "waypointsToWaypoints")
    MapData copy(MapData mapData);

    @Named("waypointsToWaypoints")
    List<WayPoint> map(List<WayPoint> wayPoints);

    static MapData clone(MapData data) {
        if (data == null) {
            return null;
        }

        return INSTANCE.copy(data);
    }
}
