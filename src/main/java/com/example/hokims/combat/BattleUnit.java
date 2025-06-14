package com.example.hokims.combat;

import com.example.hokims.model.Hero;
import com.example.hokims.model.Team;

import java.util.*;

/**
 * Represents a combat-ready unit during battle, created from a Hero instance.
 * Holds runtime attributes such as HP, status effects, and combat stats.
 */
public class BattleUnit {

    private final Hero base;   // Reference to the original Hero object
    private final Team side;   // The team this unit belongs to

    // Core combat stats
    private int hp, atk, def, agi;
    private double crit, dodge;

    // Active status effects applied to this unit
    private final List<StatusEffect> effects = new ArrayList<>();

    /**
     * Constructor to initialize a BattleUnit based on a Hero and its team.
     * Loads total stats from the Hero, with fallback defaults.
     */
    public BattleUnit(Hero hero, Team side) {
        this.base = hero;
        this.side = side;

        Map<String,Integer> s = hero.getTotalStats();  // Fetches full stat map
        this.hp    = s.getOrDefault("HP",    1000);    // Default HP = 1000
        this.atk   = s.getOrDefault("ATK",   200);     // Default ATK = 200
        this.def   = s.getOrDefault("DEF",   50);      // Default DEF = 50
        this.agi   = s.getOrDefault("AGI",   100);     // Default AGI = 100
        this.crit  = s.getOrDefault("CRIT",  10) / 100.0;  // CRIT rate as fraction
        this.dodge = s.getOrDefault("DODGE",  5) / 100.0;  // DODGE rate as fraction
    }

    /* ====== Runtime mechanics ====== */

    // Returns true if this unit is still alive
    public boolean isAlive() { return hp > 0; }

    // Apply damage to the unit (non-negative HP floor)
    public void receiveDamage(int d) {
        hp = Math.max(0, hp - d);
    }

    // Adds a new status effect and applies its immediate effect
    public void addEffect(StatusEffect e) {
        e.onApply(this);
        effects.add(e);
    }

    // Advances all status effects by one turn, removing expired ones
    public void tickEffects() {
        for (Iterator<StatusEffect> it = effects.iterator(); it.hasNext();) {
            StatusEffect e = it.next();
            e.tick(this);
            if (e.isExpired()) {
                e.onRemove(this);
                it.remove();
            }
        }
    }

    /* ====== Getters for runtime access ====== */
    public int getHp()       { return hp; }
    public int getAtk()      { return atk; }
    public int getDef()      { return def; }
    public int getAgi()      { return agi; }
    public double getCrit()  { return crit; }
    public double getDodge() { return dodge; }

    public List<StatusEffect> getEffects() { return effects; }
    public Hero getBase()   { return base; }
    public Team getSide()   { return side; }
}
