package com.example.hokims.service;

import com.example.hokims.dto.HeroRecommendation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the RecommendationService interface.
 * Loads hero statistics, matchup data, and player mastery info from JSON files,
 * then provides hero recommendations based on win rate, pick rate, player mastery, and enemy picks.
 */
public class RecommendationServiceImpl implements RecommendationService {

    private final List<Map<String, Object>> heroStats;
    private final List<Map<String, Object>> matchups;
    private final List<Map<String, Object>> masteries;

    public RecommendationServiceImpl() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // ✅ Loads JSON files from the "data" folder (relative to the project root)
            heroStats = mapper.readValue(new File("data/hero_stats.json"), new TypeReference<>() {});
            matchups  = mapper.readValue(new File("data/hero_matchups.json"), new TypeReference<>() {});
            masteries = mapper.readValue(new File("data/player_mastery.json"), new TypeReference<>() {});
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("❌ Failed to load JSON files from the /data directory. Please check paths.", e);
        }
    }

    /**
     * Generates a list of recommended heroes based on role, bans, enemy picks, and player mastery.
     *
     * @param position    Role/position filter (currently unused in this logic)
     * @param ownedOnly   Whether to restrict recommendations to owned heroes (currently unused)
     * @param enemyPicks  List of hero names picked by the enemy
     * @param bans        List of banned hero names
     * @return A sorted list of HeroRecommendation objects
     */
    @Override
    public List<HeroRecommendation> getHeroRecommendations(String position, boolean ownedOnly,
                                                           List<String> enemyPicks, List<String> bans) {

        Set<String> banned = new HashSet<>(bans);
        Set<String> enemies = new HashSet<>(enemyPicks);

        List<HeroRecommendation> results = new ArrayList<>();
        for (Map<String, Object> stat : heroStats) {
            String name = (String) stat.get("heroName");
            if (banned.contains(name)) continue;

            double winRate = ((Number) stat.get("winRate")).doubleValue();
            double pickRate = ((Number) stat.get("pickRate")).doubleValue();
            double mastery = getMastery(name); // Player-specific performance with this hero

            // Base score calculated from weighted average
            double score = winRate * 0.5 + pickRate * 0.3 + mastery * 0.2;

            // Bonus score if this hero counters any enemy picks
            for (String enemy : enemies) {
                Optional<Map<String, Object>> matchup = matchups.stream()
                        .filter(m -> m.get("heroA").equals(name) && m.get("heroB").equals(enemy))
                        .findFirst();
                if (matchup.isPresent() && ((Number) matchup.get().get("winRate")).doubleValue() > 0.55) {
                    score += 5;  // Bonus for good counter matchup
                }
            }

            // Add hero recommendation with all rounded values
            results.add(new HeroRecommendation(name,
                    round(score), round(winRate * 100), round(pickRate * 100), round(mastery * 100)));
        }

        // Return the list sorted by overall score in descending order
        return results.stream()
                .sorted(Comparator.comparingDouble(HeroRecommendation::getScore).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves average player win rate (mastery) with a given hero.
     *
     * @param hero Name of the hero
     * @return mastery score (0.0 to 1.0 range), defaults to 0.5 if unknown
     */
    private double getMastery(String hero) {
        return masteries.stream()
                .filter(m -> m.get("hero").equals(hero))
                .mapToDouble(m -> ((Number) m.get("winRate")).doubleValue())
                .average().orElse(0.5);
    }

    /**
     * Rounds a double value to 1 decimal place.
     *
     * @param val input value
     * @return rounded value
     */
    private double round(double val) {
        return Math.round(val * 10.0) / 10.0;
    }
}
