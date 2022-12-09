package red.tetracube.data.entities;

import red.tetracube.DeviceType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @Column(name = "feedback_topic", nullable = false, unique = true)
    private String feedbackTopic;

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "lwt_topic", nullable = false, unique = true)
    private String lwtTopic;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    @Column(name = "name", nullable = false)
    private String name;

    @JoinColumn(name = "environment_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Environment.class)
    private Environment environment;

    @JoinColumn(name = "house_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = House.class)
    private House house;

    public UUID getId() {
        return id;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getName() {
        return name;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public String getLwtTopic() {
        return lwtTopic;
    }

    public House getHouse() {
        return house;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }
}
