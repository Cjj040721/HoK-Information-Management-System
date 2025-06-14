package com.example.hokims.model;

/**
 * The StatBonus interface defines a set of default methods that provide
 * bonus values for various combat-related stats.
 *
 * Any class that implements this interface (e.g., Equipment, Rune)
 * can override one or more of these methods to provide actual bonus values.
 *
 * All methods return 0 by default, so implementing classes are not required
 * to override all of them unless needed.
 */
public interface StatBonus {

    /**
     * Bonus to health points (HP).
     * @return amount of HP bonus (default is 0)
     */
    default int getHpBonus()     { return 0; }

    /**
     * Bonus to attack strength.
     * @return amount of attack bonus (default is 0)
     */
    default int getAttackBonus() { return 0; }

    /**
     * Bonus to defense.
     * @return amount of defense bonus (default is 0)
     */
    default int getDefenseBonus(){ return 0; }

    /**
     * Bonus to agility (e.g., affecting turn order or speed).
     * @return amount of agility bonus (default is 0)
     */
    default int getAgilityBonus(){ return 0; }

    /**
     * Bonus to critical hit chance.
     * @return amount of crit bonus (default is 0)
     */
    default int getCritBonus()   { return 0; }

    /**
     * Bonus to dodge or evasion rate.
     * @return amount of dodge bonus (default is 0)
     */
    default int getDodgeBonus()  { return 0; }
}
