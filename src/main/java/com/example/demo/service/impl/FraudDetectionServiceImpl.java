package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Claim;
import com.example.demo.model.FraudCheckResult;
import com.example.demo.model.FraudRule;
import com.example.demo.repository.ClaimRepository;
import com.example.demo.repository.FraudCheckResultRepository;
import com.example.demo.repository.FraudRuleRepository;
import com.example.demo.service.FraudDetectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FraudDetectionServiceImpl implements FraudDetectionService {

    private final ClaimRepository claimRepository;
    private final FraudRuleRepository fraudRuleRepository;
    private final FraudCheckResultRepository resultRepository;

    public FraudDetectionServiceImpl(ClaimRepository claimRepository,
                                     FraudRuleRepository fraudRuleRepository,
                                     FraudCheckResultRepository resultRepository) {
        this.claimRepository = claimRepository;
        this.fraudRuleRepository = fraudRuleRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public FraudCheckResult evaluateClaim(Long claimId) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim not found"));

        boolean isFraud = false;
        String triggeredRuleName = null;
        String rejectionReason = null;

        Set<FraudRule> matchedRules = new HashSet<>();

        List<FraudRule> allRules = fraudRuleRepository.findAll();

        for (FraudRule rule : allRules) {
            if (ruleMatchesClaim(rule, claim)) {
                isFraud = true;
                triggeredRuleName = rule.getRuleName();
                rejectionReason = "Rule triggered: " + rule.getRuleName();
                matchedRules.add(rule);
            }
        }

        FraudCheckResult result = new FraudCheckResult(
                claim,
                isFraud,
                triggeredRuleName,
                rejectionReason,
                LocalDateTime.now() // ✅ ensure checkedAt is non-null
        );

        result.setMatchedRules(matchedRules);
        claim.setFraudCheckResult(result); // bidirectional mapping

        return resultRepository.save(result);
    }

    @Override
    public FraudCheckResult getResultByClaim(Long claimId) {
        return resultRepository.findByClaimId(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Result not found"));
    }

    // ------------------------
    // Helper method to evaluate a single rule against claim
    // Supports numeric and description rules
    // ------------------------
    private boolean ruleMatchesClaim(FraudRule rule, Claim claim) {
        if (rule.getConditionField() == null || rule.getOperator() == null || rule.getValue() == null)
            return false;

        String field = rule.getConditionField();
        String operator = rule.getOperator();
        String value = rule.getValue();

        try {
            switch (field) {
                case "claimAmount":
                    double claimValue = claim.getClaimAmount();
                    double threshold = Double.parseDouble(value);
                    switch (operator) {
                        case ">": return claimValue > threshold;
                        case "<": return claimValue < threshold;
                        case ">=": return claimValue >= threshold;
                        case "<=": return claimValue <= threshold;
                        case "=": return claimValue == threshold;
                    }
                    break;

                case "description":
                    return "contains".equalsIgnoreCase(operator)
                            && claim.getDescription() != null
                            && claim.getDescription().contains(value);

                default:
                    return false; // unknown field
            }
        } catch (Exception e) {
            return false; // parsing errors return false
        }
        return false;
    }
}
