package com.lab.equipment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_no", nullable = false, unique = true)
    private String equipmentNo;

    @Column(nullable = false)
    private String name;

    @Column(length = 100)
    private String model;

    @Column(length = 100)
    private String manufacturer;

    @Column(name = "serial_no", length = 100)
    private String serialNo;

    @Column(name = "lab_id", nullable = false)
    private Long labId;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 12, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "asset_no", length = 50)
    private String assetNo;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @Builder.Default
    private List<EquipmentCalibration> calibrations = new ArrayList<>();

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    @Builder.Default
    private List<EquipmentMaintenance> maintenances = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean needsCalibration() {
        return calibrations.stream()
            .filter(c -> c.getNextDate() != null)
            .anyMatch(c -> c.getNextDate().isBefore(LocalDate.now()));
    }
}