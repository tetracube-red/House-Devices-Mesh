package red.tetracube.smarthomeguru.data.repositories;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.hibernate.reactive.mutiny.Mutiny;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthomeguru.data.entities.Device;

@ApplicationScoped
public class DeviceRepository {
    
    private final Mutiny.SessionFactory sessionFactory;

    public DeviceRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Uni<List<Device>> getDeviceByTetraCubeId(UUID tetracubeId) {
        var sessionUni = sessionFactory.openSession();
        return sessionUni.chain(session ->
                session.createQuery(
                        """
                            select device
                            from Device device
                            join fetch device.environment environment
                            where device.tetracubeId = :tetracubeId
                            """,
                            Device.class
                        )
                        .setParameter("tetracubeId", tetracubeId)
                        .getResultList()
                        .eventually(session::close)
        );
    }
}
