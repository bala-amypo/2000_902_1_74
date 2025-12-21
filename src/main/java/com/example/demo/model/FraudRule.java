package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "fraud_rules")
public class FraudRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique rule name
    @Column(unique = true, nullable = false)
    private String ruleName;

    // Field to inspect (e.g., claimAmount)
    private String conditionField;

    // Operator: >, <, >=, <=, =
    private String operator;

    // Threshold value as string (e.g., "10000")
    private String value;

    // LOW, MEDIUM, HIGH
    private String severity;

    // Many-to-many with Claim
    @ManyToMany(mappedBy = "suspectedRules")
    private Set<Claim> claims;

    // ✅ No-arg constructor (JPA requirement)
    public FraudRule() {
    }

    // ✅ Parameterized constructor (used by testcases)
    public FraudRule(String ruleName,
                     String conditionField,
                     String operator,
                     String value,
                     String severity) {
        this.ruleName = ruleName;
        this.conditionField = conditionField;
        this.operator = operator;
        this.value = value;
        this.severity = severity;
    }

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getConditionField() {
        return conditionField;
    }

    public void setConditionField(String conditionField) {
        this.conditionField = conditionField;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Set<Claim> getClaims() {
        return claims;
    }

    public void setClaims(Set<Claim> claims) {
        this.claims = claims;
    }
}
