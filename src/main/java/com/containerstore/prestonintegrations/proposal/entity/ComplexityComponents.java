package com.containerstore.prestonintegrations.proposal.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "complexity_components")
@Data
public class ComplexityComponents implements Serializable {

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
