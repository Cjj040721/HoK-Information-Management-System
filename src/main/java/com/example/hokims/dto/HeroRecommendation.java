package com.example.hokims.dto;

/**
 * A data transfer object (DTO) representing a recommended hero
 * with relevant performance and usage statistics.
 */
public class HeroRecommendation {
    // Hero's name
    private final String name;

    // Recommendation score based on some algorithm
    private final double score;

    // Hero's win rate in recent games (e.g., 0.55 for 55%)
    private final double winRate;

    // Frequency of this hero being picked by players (e.g., 0.25 for 25%)
    private final double pickRate;

    // Player's personal mastery or familiarity with this hero (e.g., 0.8)
    private final double mastery;

    /**
     * Constructs a HeroRecommendation object with all required stats.
     *
     * @param name      the hero's name
     * @param score     overall recommendation score
     * @param winRate   recent win rate of the hero
     * @param pickRate  how often the hero is picked
     * @param mastery   player’s mastery of the hero
     */
    public HeroRecommendation(String name, double score, double winRate,
                              double pickRate, double mastery) {
        this.name = name;
        this.score = score;
        this.winRate = winRate;
        this.pickRate = pickRate;
        this.mastery = mastery;
    }

    /** @return Hero's name */
    public String getName() { return name; }

    /** @return Recommendation score */
    public double getScore() { return score; }

    /** @return Hero's win rate */
    public double getWinRate() { return winRate; }

    /** @return Hero's pick rate */
    public double getPickRate() { return pickRate; }

    /** @return Player's mastery with this hero */
    public double getMastery() { return mastery; }
}
