package red.tetracube.data.entities;

import red.tetracube.LogicState;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "switchers_actions")
public class SwitcherAction {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "logic_state", nullable = false)
    private LogicState logicState;

    @Column(name = "mqtt_command", nullable = false)
    private String mqttCommand;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "switcher_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Switcher.class)
    private Switcher switcher;

    public LogicState getLogicState() {
        return logicState;
    }
}
