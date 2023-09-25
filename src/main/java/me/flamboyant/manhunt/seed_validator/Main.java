package me.flamboyant.manhunt.seed_validator;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin implements CommandExecutor {
    private static List<Location> getCheckVillagePoints(World world) {
        return Arrays.asList(
                new Location(world, 0, 0, 0),
                new Location(world, 0, 100, 0),
                new Location(world, 0, 0, 100),
                new Location(world, 0, -100, 0),
                new Location(world, 0, 0, -100)
        );
    }

    @Override
    public void onEnable() {
        getCommand("f_check_seed").setExecutor(this);
        checkSeed(null);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (!cmd.getName().equals("f_check_seed")) return false;

        checkSeed(sender);

        return true;
    }

    private void checkSeed(CommandSender sender) {
        World nether = getServer().getWorld("world_nether");
        String message = "Fortress : ";
        Location zero = new Location(nether, 0, 50, 0);
        Location fortressLocation = nether.locateNearestStructure(zero, StructureType.NETHER_FORTRESS, 22, false);
        if (zero.distance(fortressLocation) > 350)
            fortressLocation = null;

        if (fortressLocation == null)
            message += "" + ChatColor.RED + ChatColor.BOLD + "KO" + "\n";
        else
            message += "" + ChatColor.BLUE + ChatColor.BOLD + "OK : " + "\n";

        message += ChatColor.RESET + "--------\n";
        message += ChatColor.RESET + "Check structures distances :\n";

        Map<String, StructureType> structureNameToStructureType = StructureType.getStructureTypes();
        World world = getServer().getWorld("world");
        Location worldZero = new Location(world, 0, 64, 0);

        if (sender != null) sender.sendMessage(message);
        else getServer().getConsoleSender().sendMessage(message);

        int i = 1;
        for (String structureName : structureNameToStructureType.keySet()) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                String msg2 = structureName + " : ";
                Location structureLocation = world.locateNearestStructure(worldZero, structureNameToStructureType.get(structureName), 60, false);
                if (structureLocation == null)
                    msg2 += "" + ChatColor.RED + ChatColor.BOLD + "KO" + "\n";
                else
                    msg2 += "" + ChatColor.BLUE + ChatColor.BOLD + worldZero.distance(structureLocation) + " : " + "\n";

                if (sender != null) sender.sendMessage(msg2);
                else getServer().getConsoleSender().sendMessage(msg2);
            }, 1 * i);
        }
    }
}
