package io.pjj.ziphyeonjeon.RiskAnalysis.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RISK_UPLOAD_RESULT")
public class RiskUpload {

    protected RiskUpload() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RISK_UPLOAD_ID")
    private Long riskUploadId;

    @Column(name = "RISK_ADDRESS", nullable = false)
    private String address;

    @Column(name = "SAVED_FILE", nullable = false)
    private String savedFile;

    @Column(name = "RISK_CREATED_AT")
    private LocalDateTime createdAt;

    public RiskUpload(String address, String savedFile) {
        this.address = address;
        this.savedFile = savedFile;
        this.createdAt = LocalDateTime.now();
    }

    public Long getRiskUploadId() {
        return riskUploadId;
    }

    public String getAddress() {
        return address;
    }

    public String getSavedFile() {
        return savedFile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setRiskUploadId(Long riskUploadId) {
        this.riskUploadId = riskUploadId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSavedFile(String savedFile) {
        this.savedFile = savedFile;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
