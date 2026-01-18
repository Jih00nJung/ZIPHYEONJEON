package io.pjj.ziphyeonjeon.RiskAnalysis.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DISASTER_API_CACHE")
public class DisasterApiCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CACHE_ID")
    private Long cacheId;

    @Column(name = "ADDRESS", length = 500, nullable = false)
    private String address;

    @Column(name = "LATITUDE", length = 50)
    private String lat;

    @Column(name = "LONGITUDE", length = 50)
    private String lng;

    @Column(name = "DISASTER_TYPE", length = 100)
    private String type;

    @Column(name = "DESIGN_REASON", columnDefinition = "TEXT")
    private String designReason;

    @Column(name = "API_REG_TIME", length = 100)
    private String regTime;

    // 시스템 저장 시간 (LocalDateTime 사용 권장)
    @Column(name = "FETCHED_AT")
    private LocalDateTime fetchedAt;

    public DisasterApiCache(String address, String lat, String lng,
                            String type, String designReason, String regTime) {
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
        this.designReason = designReason;
        this.regTime = regTime;
        this.fetchedAt = LocalDateTime.now();
    }

    // Getter
    public Long getCacheId() {
        return cacheId;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getType() {
        return type;
    }

    public String getDesignReason() {
        return designReason;
    }

    public String getRegTime() {
        return regTime;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }


    // Setter
    public void setAddress(String address) {
        this.address = address;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}
