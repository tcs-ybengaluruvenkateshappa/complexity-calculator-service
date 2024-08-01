package com.containerstore.prestonintegrations.proposal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "complexity_components")
@Data
public class ComplexityComponents {

    @Id
    @Column(name = "component_id")
    private UUID component_id;

    @Column(name = "complexity")
    private Integer complexity;

    @Column(name = "component_type")
    private String component_type;

    @Column(name = "component_value")
    private String component_value;

}
