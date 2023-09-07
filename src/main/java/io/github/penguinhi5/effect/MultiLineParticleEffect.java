package io.github.penguinhi5.effect;

import io.github.penguinhi5.CustomParticleEffects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiLineParticleEffect implements ParticleEffect
{
    private CustomParticleEffects _plugin;
    private int _taskID = -1;
    private ArrayList<Location> _points = new ArrayList<>();

    public MultiLineParticleEffect(CustomParticleEffects plugin)
    {
        _plugin = plugin;
    }

    @Override
    public void play(Player player, HashMap<String, String> args)
    {
        // reset effect
        if (args.containsKey("reset"))
        {
            _points.clear();
            stop();
            return;
        }

        // update loc2 and play effect
        _points.add(player.getLocation().clone());
        if (_points.size() > 1)
        {
            stop();
            runEffect(args);
        }
    }

    @Override
    public void stop()
    {
        if (_taskID != -1)
        {
            Bukkit.getScheduler().cancelTask(_taskID);
            _taskID = -1;
        }
    }

    private void runEffect(HashMap<String, String> args)
    {
        // properties
        int speed = 1;
        double distBetweenParticles = 0.25;
        ArrayList<Location> particleLocations = getParticleLocations(distBetweenParticles);

        // spawn particle
        _taskID = new BukkitRunnable()
        {
            int idx = 0;

            @Override
            public void run()
            {
                // spawn particles
                double x = particleLocations.get(idx).getX();
                double y = particleLocations.get(idx).getY();
                double z = particleLocations.get(idx).getZ();
                particleLocations.get(0).getWorld().spawnParticle(Particle.COMPOSTER, x, y, z, 1, 0, 0, 0);
                particleLocations.get(0).getWorld().spawnParticle(Particle.CRIMSON_SPORE, x, y, z, 1, 0.5, 0.5, 0.5);
                particleLocations.get(0).getWorld().spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, x, y, z, 1, 0.5, 0.5, 0.5);

                // update idx
                if (++idx >= particleLocations.size())
                    idx = 0;
            }
        }.runTaskTimer(_plugin, 0, speed).getTaskId();
    }

    private ArrayList<Location> getParticleLocations(double distBetweenParticles)
    {
        ArrayList<Location> particleLocations = new ArrayList<>();
        double[] distances = new double[_points.size() - 1];
        double totalDistance = 0;

        // compute length of all line segment
        for (int i  = 0; i < distances.length; i++)
        {
            distances[i] = _points.get(i).distance(_points.get(i + 1));
            totalDistance += distances[i];
        }

        // compute distance between particle in segment
        for (int segment = 0; segment < distances.length; segment++)
        {
            // compute distance between particle in segment
            int particleInSegment = (int)Math.max(Math.round(distances[segment] / distBetweenParticles), 1);
            double xDist = (_points.get(segment + 1).getX() - _points.get(segment).getX()) / particleInSegment;
            double yDist = (_points.get(segment + 1).getY() - _points.get(segment).getY()) / particleInSegment;
            double zDist = (_points.get(segment + 1).getZ() - _points.get(segment).getZ()) / particleInSegment;

            // compute particle locations in segment
            for (int point = 0; point < particleInSegment; point++)
            {
                double x = _points.get(segment).getX() + xDist * point;
                double y = _points.get(segment).getY() + yDist * point;
                double z = _points.get(segment).getZ() + zDist * point;
                particleLocations.add(new Location(_points.get(0).getWorld(), x, y, z));
            }
        }
        return particleLocations;
    }
}
