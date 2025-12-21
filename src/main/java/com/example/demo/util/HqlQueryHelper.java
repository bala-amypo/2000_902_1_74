package com.example.demo.util;

import com.example.demo.model.Claim;
import com.example.demo.model.Policy;
import com.example.demo.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HqlQueryHelper {

    @PersistenceContext
    private EntityManager entityManager;

    // 🔹 Get all policies for a given user
    public List<Policy> getPoliciesByUserId(Long userId) {
        String hql = "SELECT p FROM Policy p WHERE p.user.id = :userId";
        TypedQuery<Policy> query = entityManager.createQuery(hql, Policy.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    // 🔹 Get all claims for a given policy
    public List<Claim> getClaimsByPolicyId(Long policyId) {
        String hql = "SELECT c FROM Claim c WHERE c.policy.id = :policyId";
        TypedQuery<Claim> query = entityManager.createQuery(hql, Claim.class);
        query.setParameter("policyId", policyId);
        return query.getResultList();
    }

    // 🔹 Find user by email
    public User getUserByEmail(String email) {
        String hql = "SELECT u FROM User u WHERE u.email = :email";
        TypedQuery<User> query = entityManager.createQuery(hql, User.class);
        query.setParameter("email", email);
        List<User> result = query.getResultList();
        return result.isEmpty() ? null : result.get(0);
    }

    // 🔹 Generic helper for JPQL queries (optional)
    public <T> List<T> executeQuery(String jpql, Class<T> clazz) {
        TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
        return query.getResultList();
    }
}
