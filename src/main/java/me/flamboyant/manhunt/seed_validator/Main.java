package me.flamboyant.manhunt.seed_validator;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        String message = "Fortress near 0 : ";
        Location zero = new Location(nether, 0, 0, 0);
        Location fortressLocation = nether.locateNearestStructure(zero, StructureType.NETHER_FORTRESS, 22, false);
        if (zero.distance(fortressLocation) > 350)
            fortressLocation = null;

        if (fortressLocation == null)
            message += "" + ChatColor.RED + ChatColor.BOLD + "KO" + "\n";
        else
            message += "" + ChatColor.BLUE + ChatColor.BOLD + "OK : " + "\n";

        message += ChatColor.RESET + "Village near 0 : ";

        HashSet<String> villageLocations = new HashSet<>();
        int count = 0;
        for (Location loc : getCheckVillagePoints(getServer().getWorld("world"))) {
            Location villageLocation = nether.locateNearestStructure(loc, StructureType.VILLAGE, 10, false);
            if (villageLocation != null && loc.distance(villageLocation) < 160 && villageLocations.add(villageLocation.toString()))
                count++;
        }

        if (count > 0)
            message += "" + ChatColor.BLUE + ChatColor.BOLD + count + "\n";
        else
            message += "" + ChatColor.RED + ChatColor.BOLD + "0" + "\n";

        if (sender != null) sender.sendMessage(message);
        else getServer().getConsoleSender().sendMessage(message);
    }
}
