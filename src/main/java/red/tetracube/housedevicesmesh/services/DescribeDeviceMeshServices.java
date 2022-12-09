package red.tetracube.housedevicesmesh.services;

import io.smallrye.mutiny.Uni;
import red.tetracube.DescribeDeviceMeshResponse;
import red.tetracube.Device;
import red.tetracube.HouseEnvironment;
import red.tetracube.data.repositories.HouseRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DescribeDeviceMeshServices {

    private final HouseRepository houseRepository;

    public DescribeDeviceMeshServices(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public Uni<DescribeDeviceMeshResponse> getHouseDevicesMesh(UUID houseId) {
        return this.houseRepository.getHouseWithDevices(houseId)
                .map(optionalHouse -> {
                    if (optionalHouse.isEmpty()) {
                        return null;
                    }
                    var house = optionalHouse.get();
                    var devicesResponse = house.getDeviceList().stream()
                            .map(device -> {
                                var environment = Optional.ofNullable(device.getEnvironment())
                                        .map(environmentEntity ->
                                                HouseEnvironment.newBuilder()
                                                        .setId(environmentEntity.getId().toString())
                                                        .setName(environmentEntity.getName())
                                                        .build()
                                        )
                                        .orElse(null);
                                return Device.newBuilder()
                                        .setDeviceType(device.getDeviceType())
                                        .setColorCode(device.getColorCode())
                                        .setId(device.getId().toString())
                                        .setName(device.getName())
                                        .setIsOnline(device.getOnline())
                                        .setEnvironment(environment)
                                        .build();
                            })
                            .toList();
                    return DescribeDeviceMeshResponse.newBuilder()
                            .addAllDevices(devicesResponse)
                            .setName(house.getName())
                            .setId(house.getId().toString())
                            .build();
                });
    }
}
