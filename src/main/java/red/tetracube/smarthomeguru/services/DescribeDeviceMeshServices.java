package red.tetracube.smarthomeguru.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import red.tetracube.*;
import red.tetracube.smarthomeguru.data.entities.Device;
import red.tetracube.smarthomeguru.data.entities.SwitcherAction;
import red.tetracube.smarthomeguru.data.repositories.DeviceRepository;
import red.tetracube.smarthomeguru.data.repositories.SwitcherRepository;

import javax.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DescribeDeviceMeshServices {

        private final static Logger LOGGER = LoggerFactory.getLogger(DescribeDeviceMeshServices.class);

        private final SwitcherRepository switcherRepository;
        private final DeviceRepository deviceRepository;

        public DescribeDeviceMeshServices(SwitcherRepository switcherRepository, DeviceRepository deviceRepository) {
                this.switcherRepository = switcherRepository;
                this.deviceRepository = deviceRepository;
        }

        public Uni<DescribeDevicesMeshResponse> describeDevicesMesh(UUID tetracubeId) {
                var devicesUni = this.deviceRepository.getDeviceByTetraCubeId(tetracubeId);
                var devicesResponseUnis = devicesUni.onItem()
                                .transformToMulti(devices -> Multi.createFrom().items(devices.stream()))
                                .<DescribeDeviceMeshResponse>flatMap(device -> {
                                        var environment = Optional.ofNullable(device.getEnvironment())
                                                        .map(environmentEntity -> SmartHomeEnvironmentResponse
                                                                        .newBuilder()
                                                                        .setId(environmentEntity.getId().toString())
                                                                        .setName(environmentEntity.getName())
                                                                        .build())
                                                        .orElse(null);
                                        var deviceBuilder = DescribeDeviceMeshResponse.newBuilder()
                                                        .setDeviceType(device.getDeviceType())
                                                        .setColorCode(device.getColorCode())
                                                        .setId(device.getId().toString())
                                                        .setName(device.getName())
                                                        .setIsOnline(device.getIsOnline())
                                                        .setEnvironment(environment);
                                        switch (device.getDeviceType()) {
                                                case RGB_LED:
                                                        return Multi.createFrom().item(deviceBuilder.build());
                                                case SWITCHER:
                                                        return this.attachSwitcherToDevices(device)
                                                                        .map(optionalSwitcher -> {
                                                                                optionalSwitcher.ifPresent(
                                                                                                deviceBuilder::setSwitcher);
                                                                                return deviceBuilder.build();
                                                                        })
                                                                        .toMulti();
                                                case UNRECOGNIZED:
                                                        return Multi.createFrom().item(deviceBuilder.build());
                                                default:
                                                        return Multi.createFrom().item(deviceBuilder.build());
                                        }
                                })
                                .collect()
                                .asList();
                return devicesResponseUnis
                                .map(devicesResponse -> DescribeDevicesMeshResponse.newBuilder()
                                                .addAllDevices(devicesResponse)
                                                .build());

        }

        private Uni<Optional<DeviceSwitcherResponse>> attachSwitcherToDevices(Device device) {
                return this.switcherRepository.getByDevice(device.getId())
                                .map(switcher -> {
                                        if (switcher.isEmpty()) {
                                                LOGGER.warn("No switcher found for device of type switcher. This should never happen");
                                        }
                                        return switcher.map(s -> {
                                                var mappedActions = s.getSwitcherActionList().stream()
                                                                .map(SwitcherAction::getLogicState)
                                                                .toList();
                                                return DeviceSwitcherResponse.newBuilder()
                                                                .setId(s.getId().toString())
                                                                .addAllActions(mappedActions)
                                                                .build();
                                        });
                                });
        }
}
