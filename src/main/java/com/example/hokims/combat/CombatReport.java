package com.example.hokims.combat;

import com.example.hokims.model.Team;
import java.util.*;

/**
 * CombatReport stores the entire battle log between two teams, including each round's details
 * and the final winner. Used for visualizing or analyzing a simulated combat.
 */
public class CombatReport {

    /**
     * RoundRecord represents the log of a single round/action in battle.
     * @param round     The round number
     * @param attacker  Name of the attacking hero
     * @param defender  Name of the defending hero (can be null for non-targeted actions)
     * @param damage    Amount of damage dealt
     * @param actionTag Describes the action (e.g., "attack", "skill", "crit")
     * @param side      Name of the attacking team
     */
    public record RoundRecord(
            int round, String attacker, String defender,
            int damage, String actionTag, String side) {}

    private final Team teamA, teamB;                  // The two teams involved in the battle
    private final List<RoundRecord> rounds = new ArrayList<>(); // Log of each round
    private Team winner;                              // The winning team

    // Constructor: Initializes the report with two opposing teams
    public CombatReport(Team a, Team b) {
        this.teamA = a;
        this.teamB = b;
    }

    /**
     * Adds a new round record to the combat log.
     * @param rnd  Round number
     * @param src  Attacking BattleUnit
     * @param dst  Defending BattleUnit (nullable)
     * @param dmg  Damage dealt
     * @param tag  Description of action
     */
    public void add(int rnd, BattleUnit src, BattleUnit dst, int dmg, String tag) {
        rounds.add(new RoundRecord(
                rnd,
                src.getBase().getName(),
                dst == null ? null : dst.getBase().getName(),
                dmg,
                tag,
                src.getSide().getName()
        ));
    }

    // Returns the full list of round-by-round combat records
    public List<RoundRecord> getRounds() {
        return rounds;
    }

    // Sets the winner of the combat
    public void setWinner(Team w) {
        this.winner = w;
    }

    // Retrieves the winner team (null if draw)
    public Team getWinner() {
        return winner;
    }

    // Returns the two combatant teams
    public Team[] sides() {
        return new Team[]{teamA, teamB};
    }
}
