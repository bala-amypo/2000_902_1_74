package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many claims belong to one policy
    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    // Date of claim
    private LocalDate claimDate;

    // Amount requested
    private Double claimAmount;

    // Description of incident
    private String description;

    // PENDING, APPROVED, REJECTED
    private String status;

    // Many-to-many with FraudRule (suspected rules)
    @ManyToMany
    @JoinTable(
            name = "claim_fraud_rules",
            joinColumns = @JoinColumn(name = "claim_id"),
            inverseJoinColumns = @JoinColumn(name = "fraud_rule_id")
    )
    private Set<FraudRule> suspectedRules;

    // One-to-one with FraudCheckResult
    @OneToOne(mappedBy = "claim", cascade = CascadeType.ALL)
    private FraudCheckResult fraudCheckResult;

    // ✅ No-arg constructor (JPA)
    public Claim() {
    }

    // ✅ Parameterized constructor (used by testcases)
    public Claim(Policy policy,
                 LocalDate claimDate,
                 Double claimAmount,
                 String description) {
        this.policy = policy;
        this.claimDate = claimDate;
        this.claimAmount = claimAmount;
        this.description = description;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public Double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(Double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<FraudRule> getSuspectedRules() {
        return suspectedRules;
    }

    public void setSuspectedRules(Set<FraudRule> suspectedRules) {
        this.suspectedRules = suspectedRules;
    }

    public FraudCheckResult getFraudCheckResult() {
        return fraudCheckResult;
    }

    public void setFraudCheckResult(FraudCheckResult fraudCheckResult) {
        this.fraudCheckResult = fraudCheckResult;
    }
}
