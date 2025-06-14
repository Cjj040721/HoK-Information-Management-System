package com.example.hokims.combat;

/**
 * This class represents a "Stun" status effect that prevents a unit from acting
 * for a specified number of turns.
 */
public class StunEffect implements StatusEffect {
    // Number of turns remaining for the stun effect
    private int leftRounds;

    /**
     * Constructor to initialize the stun effect duration.
     * @param rounds Number of turns the unit should be stunned
     */
    public StunEffect(int rounds) {
        this.leftRounds = rounds;
    }

    /**
     * Called when the effect is first applied to a unit.
     * No additional behavior when stun is applied.
     */
    @Override
    public void onApply(BattleUnit t) {}

    /**
     * Called on each turn to update the effect.
     * Decreases the remaining stun duration.
     * @param t The affected battle unit
     */
    @Override
    public void tick(BattleUnit t) {
        --leftRounds; // Decrease the number of remaining stunned turns
    }

    /**
     * Called when the effect is removed from the unit.
     * No additional cleanup for stun.
     */
    @Override
    public void onRemove(BattleUnit t) {}

    /**
     * Checks if the stun effect has ended.
     * @return true if no turns are left, false otherwise
     */
    @Override
    public boolean isExpired() {
        return leftRounds <= 0;
    }
}
