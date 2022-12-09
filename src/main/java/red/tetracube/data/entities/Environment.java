package red.tetracube.data.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "environments")
public class Environment {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "house_id", nullable = false)
    @ManyToOne(targetEntity = House.class, fetch = FetchType.LAZY)
    private House house;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
