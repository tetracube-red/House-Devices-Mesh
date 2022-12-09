package red.tetracube.data.repositories;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import red.tetracube.data.entities.House;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class HouseRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public HouseRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Uni<Optional<House>> getHouseWithDevices(UUID houseId) {
        var sessionUni = sessionFactory.openSession();
        return sessionUni.flatMap(session ->
                session.createQuery(
                                """
                                        select house
                                        from House house
                                        left join fetch house.deviceList device
                                        left join fetch device.environment
                                        where house.id = :id
                                        """,
                                House.class
                        )
                        .setParameter("id", houseId)
                        .setMaxResults(1)
                        .getSingleResultOrNull()
                        .eventually(session::close)
                        .map(Optional::ofNullable)
        );
    }
}
