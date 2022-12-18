package red.tetracube.data.entities;

import red.tetracube.LogicState;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "switchers_history")
public class SwitcherHistory {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "logic_state", nullable = false)
    private LogicState logicState;

    @Column(name = "stored_at", nullable = false)
    private Timestamp storedAt;

    @JoinColumn(name = "switcher_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Switcher.class)
    private Switcher switcher;
}
