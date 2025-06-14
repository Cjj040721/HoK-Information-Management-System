package com.example.hokims.combat;

import com.example.hokims.model.Team;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class defines the turn-based combat simulation engine.
 * It runs a fight between two teams for a specified number of rounds.
 */
public final class TurnBasedEngine {

    // Random number generator for stochastic elements like dodging, crits, etc.
    private final Random rng = ThreadLocalRandom.current();

    /**
     * Simulates a battle between two teams for a given number of rounds.
     *
     * @param tA        Team A
     * @param tB        Team B
     * @param maxRounds Maximum number of rounds to simulate
     * @return CombatReport with the results of the simulation
     */
    public CombatReport simulate(Team tA, Team tB, int maxRounds) {
        List<BattleUnit> A = wrap(tA), B = wrap(tB);
        CombatReport rep = new CombatReport(tA, tB);

        // Loop through each round of combat
        for (int rnd = 1; rnd <= maxRounds && alive(A) && alive(B); rnd++) {
            // Determine which team acts first based on average agility
            boolean aFirst = avgAgi(A) > avgAgi(B) ||
                    (avgAgi(A) == avgAgi(B) && rng.nextBoolean());

            // First team's turn
            step(rnd, aFirst ? A : B, aFirst ? B : A, rep);
            // Second team's turn
            step(rnd, aFirst ? B : A, aFirst ? A : B, rep);

            // Process status effects like poison or stun at end of the round
            A.forEach(BattleUnit::tickEffects);
            B.forEach(BattleUnit::tickEffects);
        }

        // Determine the winner after the battle ends
        rep.setWinner(determineWinner(A, B));
        return rep;
    }

    /* ====================================== */

    /**
     * Executes one side's actions during a round.
     *
     * @param rnd  Current round number
     * @param atk  Attacking team
     * @param def  Defending team
     * @param rep  Combat report to log actions
     */
    private void step(int rnd, List<BattleUnit> atk, List<BattleUnit> def, CombatReport rep) {
        for (BattleUnit u : atk) {
            // Skip unit if dead or stunned
            if (!u.isAlive() || stunned(u)) {
                rep.add(rnd, u, null, 0, "STUNNED");
                continue;
            }

            // Select a living target with the lowest HP
            BattleUnit target = def.stream()
                    .filter(BattleUnit::isAlive)
                    .min(Comparator.comparingInt(BattleUnit::getHp))
                    .orElse(null);
            if (target == null) return;

            // Calculate and apply damage
            int dmg = damage(u, target);
            target.receiveDamage(dmg);

            // 30% chance to apply 1-round stun
            if (rng.nextDouble() < 0.3) {
                target.addEffect(new StunEffect(1));
            }

            // Record action in the combat report
            rep.add(rnd, u, target, dmg, "ATTACK");
        }
    }

    /**
     * Calculates the damage dealt from attacker to defender.
     * Includes dodge, random factor, and critical hit chance.
     */
    private int damage(BattleUnit a, BattleUnit d) {
        if (rng.nextDouble() < d.getDodge()) return 0; // Dodged

        // Random multiplier in range [0.9, 1.1]
        double f = 0.9 + rng.nextDouble() * 0.2;

        // Base damage is attack minus defense, scaled by factor
        int base = (int) Math.max(1, (a.getAtk() - d.getDef()) * f);

        // Critical hit doubles the damage
        if (rng.nextDouble() < a.getCrit()) base *= 2;

        return base;
    }

    /**
     * Checks whether a unit is currently stunned.
     */
    private boolean stunned(BattleUnit u) {
        return u.getEffects().stream().anyMatch(e -> e instanceof StunEffect);
    }

    /**
     * Converts a team into a list of BattleUnits.
     */
    private List<BattleUnit> wrap(Team t) {
        List<BattleUnit> l = new ArrayList<>();
        t.getHeroes().forEach(h -> l.add(new BattleUnit(h, t)));
        return l;
    }

    /**
     * Checks if any unit in the list is still alive.
     */
    private boolean alive(List<BattleUnit> l) {
        return l.stream().anyMatch(BattleUnit::isAlive);
    }

    /**
     * Calculates the average agility of all living units.
     */
    private double avgAgi(List<BattleUnit> l) {
        return l.stream()
                .filter(BattleUnit::isAlive)
                .mapToInt(BattleUnit::getAgi)
                .average()
                .orElse(0);
    }

    /**
     * Determines the winning team based on alive status and total HP.
     * Returns null if the result is a perfect draw.
     */
    private Team determineWinner(List<BattleUnit> A, List<BattleUnit> B) {
        boolean aAlive = alive(A), bAlive = alive(B);

        if (aAlive && !bAlive) return A.get(0).getSide();
        if (bAlive && !aAlive) return B.get(0).getSide();

        // Both have survivors, check total HP
        int hpA = A.stream().mapToInt(BattleUnit::getHp).sum();
        int hpB = B.stream().mapToInt(BattleUnit::getHp).sum();

        if (hpA != hpB) return hpA > hpB ? A.get(0).getSide() : B.get(0).getSide();

        // Perfect draw
        return null;
    }
}
