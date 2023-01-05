package red.tetracube.smarthomeguru.data.entities;

import red.tetracube.DeviceType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(schema = "smart_home_guru", name = "devices")
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

    @Column(name = "tetracube_id", nullable = false)
    private UUID tetracubeId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getFeedbackTopic() {
        return feedbackTopic;
    }

    public void setFeedbackTopic(String feedbackTopic) {
        this.feedbackTopic = feedbackTopic;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLwtTopic() {
        return lwtTopic;
    }

    public void setLwtTopic(String lwtTopic) {
        this.lwtTopic = lwtTopic;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public UUID getTetracubeId() {
        return tetracubeId;
    }

    public void setTetracubeId(UUID tetracubeId) {
        this.tetracubeId = tetracubeId;
    }
    
}
