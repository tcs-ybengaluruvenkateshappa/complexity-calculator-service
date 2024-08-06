package com.containerstore.prestonintegrations.proposal.repository;

import com.containerstore.prestonintegrations.proposal.entity.ComplexityComponents;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ComplexityComponentsRepository extends JpaRepository<ComplexityComponents, UUID> {

    @Query("SELECT c.component_value FROM ComplexityComponents c WHERE c.complexity = :complexity")
    @Cacheable(cacheNames = "complexity_value", key = "#complexityValue")
    List<String> getComponentValueByComplexity(@Param("complexity") Integer complexityValue);
}
