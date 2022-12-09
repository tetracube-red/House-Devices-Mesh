package red.tetracube.housedevicesmesh;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import red.tetracube.DescribeDeviceMeshRequest;
import red.tetracube.DescribeDeviceMeshResponse;
import red.tetracube.HouseDevicesMesh;
import red.tetracube.housedevicesmesh.services.DescribeDeviceMeshServices;

import java.util.UUID;

@GrpcService
public class HouseDevicesMeshGrpcService implements HouseDevicesMesh {

    private final DescribeDeviceMeshServices describeDeviceMeshServices;

    public HouseDevicesMeshGrpcService(DescribeDeviceMeshServices describeDeviceMeshServices) {
        this.describeDeviceMeshServices = describeDeviceMeshServices;
    }

    @Override
    public Uni<DescribeDeviceMeshResponse> describeDevicesMesh(DescribeDeviceMeshRequest request) {
        return this.describeDeviceMeshServices.getHouseDevicesMesh(
                UUID.fromString(request.getHouseId())
        );
    }
}
