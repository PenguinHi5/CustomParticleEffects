package io.github.penguinhi5.command;

import io.github.penguinhi5.CustomParticleEffects;
import io.github.penguinhi5.effect.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ParticleEffectCmd implements CommandExecutor
{
    private CustomParticleEffects _plugin;
    private HashMap<String, ParticleEffect> _particleEffects;

    public ParticleEffectCmd(CustomParticleEffects plugin, HashMap<String, ParticleEffect> particleEffects)
    {
        _plugin = plugin;
        _particleEffects = particleEffects;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player) || args.length == 0) return false;

        // /cpe stop
        // stops all particles
        if (args[0].equalsIgnoreCase("stop"))
        {
            _plugin.stopParticles();
            return true;
        }

        // invalid particle effect name
        if (!_particleEffects.containsKey(args[0]))
        {
            sender.sendMessage(ChatColor.RED + "Particle effect does not exist");
            return false;
        }

        // store particle-specific arguments
        HashMap<String, String> particleArgs = new HashMap<>();
        for (int i = 1; i < args.length; i++)
        {
            if (args[i].startsWith("--") && args[i].length() >= 3 && i + 1 < args.length)
            {
                particleArgs.put(args[i].substring(2), args[++i]);
            }
        }

        // play particle
        Player player = (Player)sender;
        _particleEffects.get(args[0].toLowerCase()).play(player, particleArgs);
        Bukkit.broadcastMessage(ChatColor.GREEN + "Starting particle effect " + ChatColor.WHITE + args[0]);
        return true;
    }
}
