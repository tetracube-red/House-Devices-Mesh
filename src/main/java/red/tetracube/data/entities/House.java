package red.tetracube.data.entities;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "houses")
public class House {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(targetEntity = Device.class, fetch = FetchType.LAZY, mappedBy = "house")
    private List<Device> deviceList;

    @OneToMany(targetEntity = Environment.class, fetch = FetchType.LAZY, mappedBy = "house")
    private List<Environment> environmentList;

    public House() {
    }

    public House(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Device> getDeviceList() {
        return deviceList;
    }
}
