package com.example.hokims.combat;

//op-level interfaces for all status effects
public interface StatusEffect {
    //Call when entering the state (modifiable attributes or play animations)
    void onApply(BattleUnit target);
    //Settled once per round
    void tick(BattleUnit target);
    //Call at the end of the state (for clearing bonuses, etc.)
    void onRemove(BattleUnit target);
    //Whether it has expired
    boolean isExpired();
}
