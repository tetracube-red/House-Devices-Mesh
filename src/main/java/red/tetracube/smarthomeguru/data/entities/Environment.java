package red.tetracube.smarthomeguru.data.entities;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "smart_home_guru", name = "environments")
public class Environment {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tetracube_id", nullable = false)
    private UUID tetracubeId;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
