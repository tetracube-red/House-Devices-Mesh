package red.tetracube.smarthomeguru;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import red.tetracube.DescribeDevicesMeshRequest;
import red.tetracube.DescribeDevicesMeshResponse;
import red.tetracube.TetracubeDevicesMesh;
import red.tetracube.smarthomeguru.services.DescribeDeviceMeshServices;

import java.util.UUID;

@GrpcService
public class HouseDevicesMeshGrpcService implements TetracubeDevicesMesh {

    private final DescribeDeviceMeshServices describeDeviceMeshServices;

    public HouseDevicesMeshGrpcService(DescribeDeviceMeshServices describeDeviceMeshServices) {
        this.describeDeviceMeshServices = describeDeviceMeshServices;
    }

    @Override
    public Uni<DescribeDevicesMeshResponse> describeDevicesMesh(DescribeDevicesMeshRequest request) {
        return this.describeDeviceMeshServices.describeDevicesMesh(
                UUID.fromString(request.getHouseId())
                );
    }
}
