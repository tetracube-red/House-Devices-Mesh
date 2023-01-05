package red.tetracube.smarthomeguru.data.repositories;

import io.smallrye.mutiny.Uni;
import red.tetracube.smarthomeguru.data.entities.Switcher;

import org.hibernate.reactive.mutiny.Mutiny;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class SwitcherRepository {

    private final Mutiny.SessionFactory sessionFactory;

    public SwitcherRepository(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Uni<Optional<Switcher>> getByDevice(UUID deviceId) {
        var sessionUni = sessionFactory.openSession();
        return sessionUni.chain(session ->
                session.createQuery(
                        """
                            from Switcher switcher
                            left join fetch switcher.switcherActionList actions
                            where switcher.device.id = :deviceId
                            """,
                            Switcher.class
                        )
                        .setParameter("deviceId", deviceId)
                        .setMaxResults(1)
                        .getSingleResultOrNull()
                        .eventually(session::close)
                        .map(Optional::ofNullable)
        );
    }
}
