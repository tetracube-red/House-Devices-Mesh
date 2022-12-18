package red.tetracube.housedevicesmesh.services;

import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.*;
import red.tetracube.data.entities.SwitcherAction;
import red.tetracube.data.repositories.HouseRepository;
import red.tetracube.data.repositories.SwitcherRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DescribeDeviceMeshServices {

    private final static Logger LOGGER = LoggerFactory.getLogger(DescribeDeviceMeshServices.class);

    private final HouseRepository houseRepository;
    private final SwitcherRepository switcherRepository;

    public DescribeDeviceMeshServices(HouseRepository houseRepository,
                                      SwitcherRepository switcherRepository) {
        this.houseRepository = houseRepository;
        this.switcherRepository = switcherRepository;
    }

    public Uni<DescribeDeviceMeshResponse> getHouseDevicesMesh(UUID houseId) {
        return this.houseRepository.getHouseWithDevices(houseId)
                .flatMap(optionalHouse -> {
                    if (optionalHouse.isEmpty()) {
                        return null;
                    }
                    var house = optionalHouse.get();
                    var devicesResponseUnis = house.getDeviceList().stream()
                            .map(device -> {
                                var environment = Optional.ofNullable(device.getEnvironment())
                                        .map(environmentEntity ->
                                                HouseEnvironment.newBuilder()
                                                        .setId(environmentEntity.getId().toString())
                                                        .setName(environmentEntity.getName())
                                                        .build()
                                        )
                                        .orElse(null);
                                if (device.getDeviceType() == DeviceType.SWITCHER) {
                                    return this.attachSwitchersToDevices(device)
                                            .map(optionalSwitcher -> {
                                                var deviceBuilder = Device.newBuilder()
                                                        .setDeviceType(device.getDeviceType())
                                                        .setColorCode(device.getColorCode())
                                                        .setId(device.getId().toString())
                                                        .setName(device.getName())
                                                        .setIsOnline(device.getOnline())
                                                        .setEnvironment(environment);
                                                optionalSwitcher.ifPresent(deviceBuilder::setSwitcher);
                                                return deviceBuilder.build();
                                            });
                                }
                                return Uni.createFrom().item(
                                        Device.newBuilder()
                                                .setDeviceType(device.getDeviceType())
                                                .setColorCode(device.getColorCode())
                                                .setId(device.getId().toString())
                                                .setName(device.getName())
                                                .setIsOnline(device.getOnline())
                                                .setEnvironment(environment)
                                                .build()
                                );
                            })
                            .toList();
                    return Uni.join().all(devicesResponseUnis).andFailFast()
                            .map(devicesResponse ->
                                    DescribeDeviceMeshResponse.newBuilder()
                                            .addAllDevices(devicesResponse)
                                            .setName(house.getName())
                                            .setId(house.getId().toString())
                                            .build()
                            );
                });
    }

    private Uni<Optional<Switcher>> attachSwitchersToDevices(red.tetracube.data.entities.Device device) {
        return this.switcherRepository.getByDevice(device.getId())
                .map(switcher -> {
                    if (switcher.isEmpty()) {
                        LOGGER.warn("No switcher found for device of type switcher. This should never happen");
                    }
                    return switcher.map(s -> {
                        var mappedActions = s.getSwitcherActionList().stream()
                                .map(SwitcherAction::getLogicState)
                                .toList();
                        return Switcher.newBuilder()
                                .setId(s.getId().toString())
                                .addAllActions(mappedActions)
                                .build();
                    });
                });
    }
}
