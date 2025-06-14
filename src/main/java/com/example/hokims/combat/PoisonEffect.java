package com.example.hokims.combat;

/**
 * This class represents a "Poison" status effect that causes damage
 * to a unit over multiple turns.
 */
public class PoisonEffect implements StatusEffect {
    // Number of rounds the poison effect lasts
    private int rounds;
    // Damage dealt per turn
    private final int dmgPerTurn;

    /**
     * Constructor to initialize the poison effect with a duration and damage per turn.
     * @param rounds Number of turns the effect should last
     * @param dmg Damage to deal each turn
     */
    public PoisonEffect(int rounds, int dmg) {
        this.rounds = rounds;
        this.dmgPerTurn = dmg;
    }

    /**
     * Called when the effect is first applied to a unit.
     * No action needed for poison when it's applied.
     */
    @Override
    public void onApply(BattleUnit t) {}

    /**
     * Called each turn to apply the effect.
     * Deals damage and decrements the remaining rounds.
     * @param t The battle unit affected by poison
     */
    @Override
    public void tick(BattleUnit t) {
        t.receiveDamage(dmgPerTurn); // Inflict damage
        --rounds;                    // Decrease remaining rounds
    }

    /**
     * Called when the effect is removed from the unit.
     * No cleanup needed for poison.
     */
    @Override
    public void onRemove(BattleUnit t) {}

    /**
     * Checks if the effect has expired.
     * @return true if no rounds are left, false otherwise
     */
    @Override
    public boolean isExpired() {
        return rounds <= 0;
    }
}
