package com.example.hokims.service;

import com.example.hokims.model.*;
import com.example.hokims.repository.DataStore;
import javafx.util.Pair;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AnalyticsService provides data analysis features for the game,
 * such as win rates, usage statistics, leaderboards, and performance trends.
 */
public class AnalyticsService {

    /* ---------------- 1. Hero Win Rate Rankings ---------------- */

    /**
     * Calculates and returns a list of heroes sorted by win rate in descending order.
     * The win rate is computed from match history stored in DataStore.
     *
     * @return List of pairs (Hero, winRate), sorted by winRate descending
     */
    public List<Pair<Hero, Double>> getHeroWinRates() {
        Map<String, long[]> counter = new HashMap<>();  // heroName → [winCount, totalGames]

        // Iterate through all recorded matches
        DataStore.matches.forEach(m -> {
            if (m.getHeroPicks() == null || m.getHeroPicks().isEmpty()) {
                System.out.println("⚠️ match " + m.getMatchId() + " has no hero picks!");
                return;
            }

            // For each player in the match, update stats for the picked hero
            m.getHeroPicks().forEach((playerName, heroName) -> {
                System.out.println("🔍 " + playerName + " picked " + heroName);
                long[] stats = counter.computeIfAbsent(heroName, k -> new long[2]);
                stats[1]++; // Increment total games played

                // Check if player's team won the match
                String playerTeam = m.getTeamA().getPlayers().contains(playerName)
                        ? m.getTeamA().getName()
                        : m.getTeamB().getName();
                if (playerTeam.equals(m.getWinner())) stats[0]++; // Increment win count
            });
        });

        // Print debug info
        System.out.println("Loaded Heroes = " + DataStore.heroes.size());
        counter.forEach((k, v) -> System.out.println(k + ": " + v[0] + " wins / " + v[1] + " games"));

        // Convert stats to Pair<Hero, winRate> and sort descending
        return DataStore.heroes.stream()
                .filter(h -> counter.containsKey(h.getName()))
                .map(h -> {
                    long[] stats = counter.get(h.getName());
                    return new Pair<>(h, stats[0] * 1.0 / stats[1]);
                })
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .collect(Collectors.toList());
    }

    /* ---------------- 2. Equipment Usage & Win Rate ---------------- */

    /**
     * Returns statistics on equipment usage and approximate win rate.
     * Since match data currently lacks equipment tracking, win rates may be NaN.
     *
     * @return List of pairs (Equipment, [usageCount, winRate]), sorted by usageCount descending
     */
    public List<Pair<Equipment, Pair<Integer, Double>>> getEquipUsageAndWin() {
        // Map equipment ID → Equipment
        Map<String, Equipment> equipmentMap = new HashMap<>();
        for (Equipment e : DataStore.equipments) {
            equipmentMap.put(e.getId(), e);
        }

        Map<String, Integer> usageMap = new HashMap<>(); // equipment ID → usage count
        Map<String, Integer> winMap = new HashMap<>();   // equipment ID → win count

        // Iterate through matches
        for (Match match : DataStore.matches) {
            Map<String, String> picks = match.getHeroPicks(); // playerName → heroName
            if (picks == null || picks.isEmpty()) continue;

            List<TeamInMatch> teams = List.of(match.getTeamA(), match.getTeamB());

            for (TeamInMatch team : teams) {
                boolean isWin = team.getName().equals(match.getWinner());

                for (String playerName : team.getPlayers()) {
                    String heroName = picks.get(playerName);
                    if (heroName == null) continue;

                    // Find the corresponding Hero object
                    Hero hero = DataStore.heroes.stream()
                            .filter(h -> h.getName().equals(heroName))
                            .findFirst()
                            .orElse(null);

                    if (hero != null && hero.getEquipments() != null) {
                        for (Equipment eq : hero.getEquipments()) {
                            String eqId = eq.getId();
                            usageMap.put(eqId, usageMap.getOrDefault(eqId, 0) + 1);
                            if (isWin) {
                                winMap.put(eqId, winMap.getOrDefault(eqId, 0) + 1);
                            }
                        }
                    }
                }
            }
        }

        // Compile results
        List<Pair<Equipment, Pair<Integer, Double>>> result = new ArrayList<>();
        for (String eqId : usageMap.keySet()) {
            Equipment equipment = equipmentMap.get(eqId);
            int usageCount = usageMap.get(eqId);
            int winCount = winMap.getOrDefault(eqId, 0);
            double winRate = usageCount > 0 ? (double) winCount / usageCount : 0.0;

            result.add(new Pair<>(equipment, new Pair<>(usageCount, winRate)));
        }

        // Sort by usage count descending
        result.sort((a, b) -> b.getValue().getKey() - a.getValue().getKey());
        return result;
    }

    /* ---------------- 3. Top N Players by Win Rate ---------------- */

    /**
     * Returns the top N players sorted by win rate in descending order.
     *
     * @param n number of players to return
     * @return List of top N players
     */
    public List<Player> getTopPlayers(int n) {
        return DataStore.players.stream()
                .sorted(Comparator.comparingDouble(Player::getWinRate).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    /* ---------------- 4. Team Score Trends (KDA-like Metrics) ---------------- */

    /**
     * Generates a historical trend of team scores from recent matches.
     * This is useful for charting team performance over time.
     *
     * @param recent number of most recent matches to include per team
     * @return Map<teamName, List<[date, score]>>
     */
    public Map<String, List<Pair<String, Double>>> getTeamKDATrend(int recent) {

        // Sort matches by timestamp in descending (most recent first) order
        List<Match> sorted = DataStore.matches.stream()
                .sorted(Comparator.comparing(Match::getTimestamp).reversed())
                .collect(Collectors.toList());

        Map<String, List<Pair<String, Double>>> trend = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd"); // Format for chart labels

        for (Match m : sorted) {
            String date = m.getTimestamp().format(fmt);

            // Process Team A
            trend.computeIfAbsent(m.getTeamA().getName(), k -> new ArrayList<>());
            List<Pair<String, Double>> listA = trend.get(m.getTeamA().getName());
            if (listA.size() < recent) {
                listA.add(new Pair<>(date, (double) m.getTeamA().getScore()));
            }

            // Process Team B
            trend.computeIfAbsent(m.getTeamB().getName(), k -> new ArrayList<>());
            List<Pair<String, Double>> listB = trend.get(m.getTeamB().getName());
            if (listB.size() < recent) {
                listB.add(new Pair<>(date, (double) m.getTeamB().getScore()));
            }
        }

        return trend;
    }
}
