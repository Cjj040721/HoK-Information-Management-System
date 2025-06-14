package com.example.hokims.service;

import com.example.hokims.dto.HeroRecommendation;

import java.util.List;

public interface RecommendationService {

    /**
     * @param position
     * @param ownedOnly
     * @param enemyPicks
     * @param bans
     */
    List<HeroRecommendation> getHeroRecommendations(String position,
                                                    boolean ownedOnly,
                                                    List<String> enemyPicks,
                                                    List<String> bans);
}
