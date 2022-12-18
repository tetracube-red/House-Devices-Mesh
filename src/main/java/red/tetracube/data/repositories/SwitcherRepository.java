package red.tetracube.data.repositories;

import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import red.tetracube.data.entities.Switcher;

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
        return sessionUni.flatMap(session ->
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
