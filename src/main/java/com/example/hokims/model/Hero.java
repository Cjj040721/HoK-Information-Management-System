package com.example.hokims.model;

import lombok.*;
import java.util.*;

/**
 * The Hero class represents a playable character in the game with base stats and
 * the ability to equip gear and runes that enhance their performance in battle.
 *
 * It implements the Identifiable interface, allowing the hero to be referenced by a unique ID.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hero implements Identifiable {

    // Unique identifier for the hero
    private String id;

    // Hero's name (e.g., "Aegis", "Shadowfang")
    private String name;

    // Hero's type or class (e.g., "Warrior", "Mage", "Assassin")
    private String type;

    // List of equipped items that boost stats
    private List<Equipment> equipments;

    // List of attached runes that also boost stats
    private List<Rune> runes;

    // Base HP (health points) before applying bonuses
    private int baseHp;

    // Base attack value before applying bonuses
    private int baseAttack;

    /**
     * Computes the total battle stats of the hero by adding bonuses
     * from equipment and runes to the base attributes.
     *
     * Currently tracks:
     * - HP (Health Points)
     * - ATK (Attack)
     * - DEF (Defense)
     * - AGI (Agility)
     * - CRIT (Critical Hit Rate)
     * - DODGE (Evasion Rate)
     *
     * @return Map containing all aggregated battle stats
     */
    public Map<String, Integer> getTotalStats() {
        // Initialize base stats
        int hp     = baseHp;
        int atk    = baseAttack;
        int def    = 50;   // Default base defense
        int agi    = 100;  // Default base agility
        int crit   = 10;   // 10% critical hit rate
        int dodge  = 5;    // 5% dodge rate

        // Add bonuses from equipment, if any
        if (equipments != null) {
            for (Equipment e : equipments) {
                hp    += safe(e::getHpBonus);
                atk   += safe(e::getAttackBonus);
                def   += safe(e::getDefenseBonus);
                agi   += safe(e::getAgilityBonus);
                crit  += safe(e::getCritBonus);
                dodge += safe(e::getDodgeBonus);
            }
        }

        // Add bonuses from runes, if any
        if (runes != null) {
            for (Rune r : runes) {
                hp    += safe(r::getHpBonus);
                atk   += safe(r::getAttackBonus);
                def   += safe(r::getDefenseBonus);
                agi   += safe(r::getAgilityBonus);
                crit  += safe(r::getCritBonus);
                dodge += safe(r::getDodgeBonus);
            }
        }

        // Store the final stat values in a map
        Map<String, Integer> map = new HashMap<>();
        map.put("HP", hp);
        map.put("ATK", atk);
        map.put("DEF", def);
        map.put("AGI", agi);
        map.put("CRIT", crit);
        map.put("DODGE", dodge);
        return map;
    }

    /**
     * Safe wrapper to avoid null pointer exceptions when retrieving integer values.
     * Returns 0 if any exception occurs.
     */
    private static int safe(IntSupplierExt s) {
        try { return s.getAsInt(); }
        catch (Exception e) { return 0; }
    }

    /**
     * Functional interface for safely retrieving integer values with lambda support.
     */
    @FunctionalInterface
    private interface IntSupplierExt {
        int getAsInt();
    }

    /**
     * Returns the unique ID of the hero.
     */
    @Override
    public String getId() {
        return id;
    }

    /* ==================== Unused private helper ==================== */
    /**
     * A helper method (currently unused) to apply bonuses from a collection of StatBonus providers.
     * @param list  Collection of StatBonus (e.g., Equipment, Runes)
     * @param hp    HP bonus accumulator
     * @param atk   Attack bonus accumulator
     * @param def   Defense bonus accumulator
     * @param agi   Agility bonus accumulator
     * @param crit  Critical bonus accumulator
     * @param dodge Dodge bonus accumulator
     */
    private void applyBonus(Collection<? extends StatBonus> list,
                            int hp, int atk, int def, int agi, int crit, int dodge) {
        if (list == null) return;
        for (StatBonus s : list) {
            hp    += s.getHpBonus();
            atk   += s.getAttackBonus();
            def   += s.getDefenseBonus();
            agi   += s.getAgilityBonus();
            crit  += s.getCritBonus();
            dodge += s.getDodgeBonus();
        }
    }
}
