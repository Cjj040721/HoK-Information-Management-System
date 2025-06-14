package com.example.hokims.service;

import com.example.hokims.model.Equipment;
import com.example.hokims.model.Hero;
import com.example.hokims.model.Player;
import com.example.hokims.repository.DataStore;

import java.util.*;
import java.util.stream.Collectors;

public class HeroService {

    // Retrieves a list of all heroes from the data store
    public List<Hero> getAllHeroes() {
        return DataStore.heroes;
    }

    // Retrieves a list of heroes that match a specific type (e.g., Warrior, Mage)
    public List<Hero> getHeroesByType(String type) {
        return DataStore.heroes.stream()
                .filter(h -> h.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    // Retrieves a list of heroes that have a specific equipment by its name
    public List<Hero> getHeroesByEquipment(String equipmentName) {
        return DataStore.heroes.stream()
                .filter(hero -> hero.getEquipments() != null &&
                        hero.getEquipments().stream()
                                .anyMatch(eq -> eq.getName().equalsIgnoreCase(equipmentName)))
                .collect(Collectors.toList());
    }

    // Counts how many times each piece of equipment is used across all heroes
    public Map<String, Long> getEquipmentUsageCount() {
        return DataStore.heroes.stream()
                .filter(hero -> hero.getEquipments() != null)
                .flatMap(hero -> hero.getEquipments().stream())
                .collect(Collectors.groupingBy(Equipment::getName, Collectors.counting()));
    }

    // Finds and returns a list of players who own a hero identified by name or ID
    public List<Player> getOwnersOfHero(String heroNameOrId) {
        return DataStore.players.stream()
                .filter(player -> player.getHeroes().stream()
                        .anyMatch(heroId -> {
                            Hero hero = DataStore.heroes.stream()
                                    .filter(h -> h.getId().equalsIgnoreCase(heroId))
                                    .findFirst().orElse(null);
                            return hero != null && (
                                    hero.getId().equalsIgnoreCase(heroNameOrId) ||
                                            hero.getName().equalsIgnoreCase(heroNameOrId)
                            );
                        }))
                .collect(Collectors.toList());
    }
}
