package red.tetracube.smarthomeguru.data.entities;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(schema = "smart_home_guru", name = "switchers")
public class Switcher {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "command_topic", nullable = false, unique = true)
    private String commandTopic;

    @JoinColumn(name = "device_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Device.class, optional = false)
    private Device device;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = SwitcherAction.class, mappedBy = "switcher")
    private List<SwitcherAction> switcherActionList;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = SwitcherHistory.class, mappedBy = "switcher")
    private List<SwitcherHistory> switcherHistoryList;

    public UUID getId() {
        return id;
    }

    public String getCommandTopic() {
        return commandTopic;
    }

    public Device getDevice() {
        return device;
    }

    public List<SwitcherAction> getSwitcherActionList() {
        return switcherActionList;
    }
}
