package com.example.hokims.ui;

import com.example.hokims.model.Hero;
import com.example.hokims.model.Player;
import com.example.hokims.service.HeroService;
import com.example.hokims.service.PlayerService;
import com.example.hokims.service.TeamService;
import com.example.hokims.repository.DataStore;
import com.example.hokims.model.Equipment;
import com.example.hokims.model.Team;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;


import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {

    private final PlayerService playerService = new PlayerService();
    private final HeroService heroService = new HeroService();
    private final Scanner scanner = new Scanner(System.in);
    private final TeamService teamService = new TeamService();

    public void start() {
        int choice = -1;

        while (choice != 13) {
            System.out.println("\n====== Honor of Kings Info System ======");
            System.out.println("1. View all players");
            System.out.println("2. View players sorted by win rate");
            System.out.println("3. View all heroes");
            System.out.println("4. View heroes by type");
            System.out.println("5. View heroes by equipment name");
            System.out.println("6. View all teams");
            System.out.println("7. View team members");
            System.out.println("8. View team average win rate");
            System.out.println("9. Create new player");
            System.out.println("10. Create new hero");
            System.out.println("11. Create new team");
            System.out.println("12. Save data to JSON files");
            System.out.println("13. Search player by ID");
            System.out.println("14. Search player by name");
            System.out.println("15. Find hero by name or type");
            System.out.println("16. Find team by name");
            System.out.println("17. View team average level");
            System.out.println("18. View top N teams by average level");
            System.out.println("19. View top N players by win rate");
            System.out.println("20. View top N heroes by usage frequency");
            System.out.println("21. View top N equipments by usage frequency");
            System.out.println("22. Find players who own a specific hero");
            System.out.println("23. Delete player by ID");
            System.out.println("24. Delete hero by ID");
            System.out.println("25. Delete team by name");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");


            try {
                choice = Integer.parseInt(scanner.nextLine());
                handleChoice(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        System.out.println("Exiting... Goodbye!");
    }

    private void handleChoice(int choice) {
        switch (choice) {
            case 0 -> System.out.println("Thanks for using the system!");
            case 1 -> playerService.getAllPlayers().forEach(System.out::println);
            case 2 -> playerService.getPlayersByWinRateDesc().forEach(System.out::println);
            case 3 -> heroService.getAllHeroes().forEach(System.out::println);
            case 4 -> {
                System.out.print("Enter hero type (e.g. Mage, Marksman): ");
                String type = scanner.nextLine();
                List<Hero> heroesByType = heroService.getHeroesByType(type);
                if (heroesByType.isEmpty()) {
                    System.out.println("No heroes found with that type.");
                } else {
                    heroesByType.forEach(System.out::println);
                }
            }
            case 5 -> {
                System.out.print("Enter equipment name: ");
                String equipName = scanner.nextLine();
                List<Hero> heroesWithEquip = heroService.getHeroesByEquipment(equipName);
                if (heroesWithEquip.isEmpty()) {
                    System.out.println("No heroes use that equipment.");
                } else {
                    heroesWithEquip.forEach(System.out::println);
                }
            }
            case 6 -> teamService.getAllTeams().forEach(System.out::println);
            case 7 -> {
                System.out.print("Enter team name: ");
                String name = scanner.nextLine();
                List<Player> members = teamService.getTeamMembers(name);
                if (members.isEmpty()) {
                    System.out.println("No such team or team has no members.");
                } else {
                    members.forEach(System.out::println);
                }
            }
            case 8 -> {
                System.out.print("Enter team name: ");
                String name = scanner.nextLine();
                double avgWinRate = teamService.getTeamAverageWinRate(name);
                System.out.println("Average win rate: " + avgWinRate);
            }
            case 9 -> createNewPlayer();
            case 10 -> createNewHero();
            case 11 -> createNewTeam();
            case 12 -> {
                DataStore.save();
                System.out.println("✅ Data saved to /data/*.json");
            }
            case 13 -> searchPlayerById();
            case 14 -> searchPlayerByName();
            case 15 -> findHero();
            case 16 -> findTeam();
            case 17 -> viewTeamAverageLevel();
            case 18 -> {
                System.out.print("Enter number of top teams to display: ");
                int topN = Integer.parseInt(scanner.nextLine());
                viewTopTeamsByAverageLevel(topN);
            }
            case 19 -> viewTopNPlayersByWinRate();
            case 20 -> viewTopNHeroesByUsage();
            case 21 -> viewTopEquipmentsByUsage();
            case 22 -> {
                System.out.print("Enter hero name or ID: ");
                String input = scanner.nextLine();
                List<Player> owners = heroService.getOwnersOfHero(input);

                if (owners.isEmpty()) {
                    System.out.println("❌ No players own this hero.");
                } else {
                    System.out.println("✅ Players who own the hero:");
                    owners.forEach(System.out::println);
                }
            }
            case 23 -> {
                System.out.print("Enter player ID to delete: ");
                String id = scanner.nextLine();
                boolean removed = DataStore.players.removeIf(p -> p.getId().equalsIgnoreCase(id));
                System.out.println(removed ? "✅ Player deleted." : "❌ Player not found.");
            }

            case 24 -> {
                System.out.print("Enter hero ID to delete: ");
                String id = scanner.nextLine();
                boolean removed = DataStore.heroes.removeIf(h -> h.getId().equalsIgnoreCase(id));
                System.out.println(removed ? "✅ Hero deleted." : "❌ Hero not found.");
            }

            case 25 -> {
                System.out.print("Enter team name to delete: ");
                String name = scanner.nextLine();
                boolean removed = DataStore.teams.removeIf(t -> t.getName().equalsIgnoreCase(name));
                if (removed) {
                    // Also remove team reference from players
                    DataStore.players.forEach(p -> {
                        if (p.getTeam() != null && p.getTeam().getName().equalsIgnoreCase(name)) {
                            p.setTeam(null);
                        }
                    });
                    System.out.println("✅ Team deleted.");
                } else {
                    System.out.println("❌ Team not found.");
                }
            }

            default -> System.out.println("Invalid option.");
        }
    }
    private void createNewPlayer() {
        System.out.println("=== Create New Player ===");

        System.out.print("Enter player name: ");
        String name = scanner.nextLine();

        System.out.print("Enter player level: ");
        int level = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter player win rate (e.g., 0.75): ");
        double winRate = Double.parseDouble(scanner.nextLine());

        List<Hero> selectedHeroes = new ArrayList<>();
        if (!DataStore.heroes.isEmpty()) {
            System.out.println("Available Heroes:");
            for (int i = 0; i < DataStore.heroes.size(); i++) {
                Hero h = DataStore.heroes.get(i);
                System.out.println((i + 1) + ". " + h.getName() + " (" + h.getType() + ")");
            }

            System.out.print("How many heroes to bind to this player? ");
            int count = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.print("Enter hero number #" + (i + 1) + ": ");
                try {
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < DataStore.heroes.size()) {
                        selectedHeroes.add(DataStore.heroes.get(index));
                    } else {
                        System.out.println("⚠️ Invalid number: out of range.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid input, must be a number.");
                }
            }
        } else {
            System.out.println("⚠️ No heroes available to assign.");
        }

        // Automatically generate ID
        String id = "P" + String.format("%03d", DataStore.players.size() + 1);

        Player player = new Player();
        player.setId(id);
        player.setName(name);
        player.setLevel(level);
        player.setWinRate(winRate);
        player.setHeroes(List.of("H1001", "H1002"));

        DataStore.players.add(player);

        System.out.println("✅ Player created: " + player.getName() + " (Level " + level + ")");
        if (!selectedHeroes.isEmpty()) {
            System.out.println("  Assigned Heroes:");
            selectedHeroes.forEach(h -> System.out.println("  - " + h.getName()));
        }
    }

    private void createNewHero() {

        System.out.println("=== Create New Hero ===");

        System.out.print("Enter hero name: ");
        String name = scanner.nextLine();

        System.out.print("Enter hero type (e.g. Mage, Tank, Marksman): ");
        String type = scanner.nextLine();

        if (DataStore.equipments.isEmpty()) {
            System.out.println("⚠️ No equipment available. Skipping equipment assignment.");
        }

        List<Equipment> heroEquipments = new ArrayList<>();

        if (!DataStore.equipments.isEmpty()) {
            System.out.println("Available Equipments:");
            for (int i = 0; i < DataStore.equipments.size(); i++) {
                Equipment e = DataStore.equipments.get(i);
                System.out.println((i + 1) + ". " + e.getName() );
            }

            System.out.print("How many equipments to add? ");
            int count = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.print("Enter equipment number #" + (i + 1) + ": ");
                try {
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < DataStore.equipments.size()) {
                        heroEquipments.add(DataStore.equipments.get(index));
                    } else {
                        System.out.println("⚠️ Invalid number: out of range.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid input, must be a number.");
                }
            }
        }

        // Automatically generate the hero ID
        String id = "H" + String.format("%03d", DataStore.heroes.size() + 1);


        Hero hero = Hero.builder()
                .id(id)
                .name(name)
                .type(type)
                .equipments(new ArrayList<>())
                .runes(new ArrayList<>()) // 先传空符文列表
                .build();

        DataStore.heroes.add(hero);

        System.out.println("✅ Hero added: " + hero.getName());
        if (!heroEquipments.isEmpty()) {
            System.out.println("  Equipments:");
            heroEquipments.forEach(e -> System.out.println("  - " + e.getName()));
        }
    }


    private void createNewTeam() {
        System.out.println("=== Create New Team ===");

        System.out.print("Enter team name: ");
        String name = scanner.nextLine();

        List<Player> members = new ArrayList<>();

        if (DataStore.players.isEmpty()) {
            System.out.println("⚠️ No players available to add.");
        } else {
            System.out.println("Available Players:");
            for (int i = 0; i < DataStore.players.size(); i++) {
                Player p = DataStore.players.get(i);
                System.out.println((i + 1) + ". " + p.getName() + " (Level " + p.getLevel() + ")");
            }

            System.out.print("How many players to add to this team? ");
            int count = Integer.parseInt(scanner.nextLine());

            for (int i = 0; i < count; i++) {
                System.out.print("Enter player number #" + (i + 1) + ": ");
                try {
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < DataStore.players.size()) {
                        Player p = DataStore.players.get(index);
                        members.add(p);
                    } else {
                        System.out.println("⚠️ Invalid number: out of range.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid input, must be a number.");
                }
            }
        }

        Team team = new Team();
        team.setName(name);
        team.setMembers(members);

        // Set the team to which the player belongs as well
        for (Player p : members) {
            p.setTeam(team);
        }

        DataStore.teams.add(team);

        System.out.println("✅ Team created: " + name);
        if (!members.isEmpty()) {
            System.out.println("  Members:");
            members.forEach(p -> System.out.println("  - " + p.getName()));
        }
    }

    private void searchPlayerById() {
        System.out.print("Enter player ID (e.g. P001): ");
        String inputId = scanner.nextLine();

        Optional<Player> found = DataStore.players.stream()
                .filter(p -> p.getId().equalsIgnoreCase(inputId))
                .findFirst();

        if (found.isPresent()) {
            Player p = found.get();
            System.out.println("✅ Player found:");
            printPlayerDetail(p);
        } else {
            System.out.println("❌ No player found with ID: " + inputId);
        }
    }

    private void searchPlayerByName() {
        System.out.print("Enter player name: ");
        String inputName = scanner.nextLine();

        List<Player> matches = DataStore.players.stream()
                .filter(p -> p.getName().equalsIgnoreCase(inputName))
                .collect(Collectors.toList());

        if (matches.isEmpty()) {
            System.out.println("❌ No player found with name: " + inputName);
        } else {
            System.out.println("✅ " + matches.size() + " player(s) found:");
            matches.forEach(this::printPlayerDetail);
        }
    }

    private void printPlayerDetail(Player p) {
        System.out.println("ID: " + p.getId());
        System.out.println("Name: " + p.getName());
        System.out.println("Level: " + p.getLevel());
        System.out.println("Win Rate: " + p.getWinRate());

        if (p.getHeroes() != null && !p.getHeroes().isEmpty()) {
            System.out.println("Heroes:");
            p.getHeroes().forEach(id -> {
                Hero hero = DataStore.heroes.stream()
                        .filter(h -> h.getId().equals(id))
                        .findFirst().orElse(null);
                if (hero != null) {
                    System.out.println(" - " + hero.getName());
                } else {
                    System.out.println(" - Unknown Hero ID: " + id);
                }
            });
        } else {
            System.out.println("Heroes: None");
        }
    }

    private void findHero() {
        System.out.print("Enter hero name or type: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        List<Hero> results = DataStore.heroes.stream()
                .filter(h -> h.getName().toLowerCase().contains(keyword) || h.getType().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("❌ No matching heroes found.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private void findTeam() {
        System.out.print("Enter team name: ");
        String name = scanner.nextLine().trim().toLowerCase();

        Optional<Team> team = DataStore.teams.stream()
                .filter(t -> t.getName().toLowerCase().equals(name))
                .findFirst();

        if (team.isPresent()) {
            System.out.println("✅ Team found: " + team.get().getName());
            team.get().getMembers().forEach(System.out::println);
        } else {
            System.out.println("❌ No team found with that name.");
        }
    }

    private void viewTeamAverageLevel() {
        System.out.print("Enter team name: ");
        String name = scanner.nextLine().trim().toLowerCase();

        Optional<Team> teamOpt = DataStore.teams.stream()
                .filter(t -> t.getName().toLowerCase().equals(name))
                .findFirst();

        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();
            List<Player> members = team.getMembers();

            if (members.isEmpty()) {
                System.out.println("⚠️ This team has no members.");
                return;
            }

            double avgLevel = members.stream()
                    .mapToInt(Player::getLevel)
                    .average()
                    .orElse(0.0);

            System.out.println("📊 Average level of team \"" + team.getName() + "\": " + avgLevel);
        } else {
            System.out.println("❌ Team not found.");
        }
    }

    private void viewTopTeamsByAverageLevel(int n) {
        List<Team> teamsWithMembers = DataStore.teams.stream()
                .filter(team -> team.getMembers() != null && !team.getMembers().isEmpty())
                .collect(Collectors.toList());

        teamsWithMembers.stream()
                .map(team -> new AbstractMap.SimpleEntry<>(
                        team,
                        team.getMembers().stream().mapToInt(Player::getLevel).average().orElse(0.0)
                ))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(n)
                .forEach(entry ->
                        System.out.println("🏆 " + entry.getKey().getName() + " - Average Level: " + entry.getValue())
                );
    }

    private void viewTopNPlayersByWinRate() {
        System.out.print("Enter N (number of top players to show): ");
        try {
            int n = Integer.parseInt(scanner.nextLine());
            if (n <= 0) {
                System.out.println("❌ Please enter a positive number.");
                return;
            }

            List<Player> sortedPlayers = DataStore.players.stream()
                    .sorted(Comparator.comparingDouble(Player::getWinRate).reversed())
                    .limit(n)
                    .toList();

            if (sortedPlayers.isEmpty()) {
                System.out.println("⚠️ No players found.");
            } else {
                System.out.println("🏆 Top " + n + " Players by Win Rate:");
                for (int i = 0; i < sortedPlayers.size(); i++) {
                    Player p = sortedPlayers.get(i);
                    System.out.printf("%d. %s (%.2f%%)\n", i + 1, p.getName(), p.getWinRate() * 100);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number.");
        }
    }

    private void viewTopNHeroesByUsage() {
        System.out.print("Enter N (number of top heroes to show): ");
        try {
            int n = Integer.parseInt(scanner.nextLine());
            if (n <= 0) {
                System.out.println("❌ Please enter a positive number.");
                return;
            }

            Map<Hero, Long> usageMap = DataStore.players.stream()
                    .flatMap(p -> p.getHeroes().stream())
                    .map(id -> DataStore.heroes.stream()
                            .filter(h -> h.getId().equals(id))
                            .findFirst().orElse(null))
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.groupingBy(h -> h, Collectors.counting()));


            if (usageMap.isEmpty()) {
                System.out.println("⚠️ No hero usage data found.");
                return;
            }

            List<Map.Entry<Hero, Long>> topHeroes = usageMap.entrySet().stream()
                    .sorted(Map.Entry.<Hero, Long>comparingByValue().reversed())
                    .limit(n)
                    .toList();

            System.out.println("🔥 Top " + n + " Most Used Heroes:");
            for (int i = 0; i < topHeroes.size(); i++) {
                Map.Entry<Hero, Long> entry = topHeroes.get(i);
                Hero h = entry.getKey();
                System.out.printf("%d. %s (%s) - Used %d times\n",
                        i + 1, h.getName(), h.getType(), entry.getValue());
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
        }
    }

    private void viewTopEquipmentsByUsage() {
        System.out.print("Enter N for top N equipments: ");
        int n = Integer.parseInt(scanner.nextLine());

        Map<String, Long> equipmentUsage = DataStore.heroes.stream()
                .filter(hero -> hero.getEquipments() != null)
                .flatMap(hero -> hero.getEquipments().stream())
                .collect(Collectors.groupingBy(Equipment::getName, Collectors.counting()));


        equipmentUsage.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .forEach(entry ->
                        System.out.println("🏹 " + entry.getKey() + " - Used by " + entry.getValue() + " heroes")
                );
    }

}

