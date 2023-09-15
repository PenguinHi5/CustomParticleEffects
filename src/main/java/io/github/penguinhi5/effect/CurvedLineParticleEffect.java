package io.github.penguinhi5.effect;

import io.github.penguinhi5.CustomParticleEffects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class CurvedLineParticleEffect implements ParticleEffect
{
    private CustomParticleEffects _plugin;
    private int _taskID = -1;
    private ArrayList<Location> _points = new ArrayList<>();

    public CurvedLineParticleEffect(CustomParticleEffects plugin)
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
        double approxDistBetweenParticles = 0.25;
        ArrayList<Location> particleLocations = getParticleLocations(approxDistBetweenParticles);

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

    private ArrayList<Location> getParticleLocations(double approxDistBetweenParticles)
    {
        ArrayList<Location> pointsList = getPoints();
        double[] distances = new double[pointsList.size() - 1];
        double totalDistance = 0;

        // compute length of all line segments
        for (int i = 0; i < distances.length; i++)
        {
            distances[i] = pointsList.get(i).distance(pointsList.get(i + 1));
            totalDistance += distances[i];
        }

        // compute particle count
        int approxParticleCount = (int)(totalDistance / approxDistBetweenParticles) + 1;
        ArrayList<Location> particleLocations = new ArrayList<>();

        // compute locations of all particles
        for (int segment = 0; segment < distances.length; segment++)
        {
            int particlesInSegment = (int)(approxParticleCount * distances[segment] / totalDistance);
            for (int point = 0; point < particlesInSegment; point++)
            {
                double t = (double)point / particlesInSegment;

                if (segment < 1)
                {
                    double x = computeInterpolation(pointsList.get(0).getX(),
                            pointsList.get(0).getX(),
                            pointsList.get(1).getX(),
                            pointsList.get(2).getX(),
                            t);
                    double y = computeInterpolation(pointsList.get(0).getY(),
                            pointsList.get(0).getY(),
                            pointsList.get(1).getY(),
                            pointsList.get(2).getY(),
                            t);
                    double z = computeInterpolation(pointsList.get(0).getZ(),
                            pointsList.get(0).getZ(),
                            pointsList.get(1).getZ(),
                            pointsList.get(2).getZ(),
                            t);
                    particleLocations.add(new Location(pointsList.get(0).getWorld(), x, y, z));
                }
                else if (segment == pointsList.size() - 1)
                {
                    double x = computeInterpolation(pointsList.get(pointsList.size() - 2).getX(),
                            pointsList.get(pointsList.size() - 1).getX(),
                            pointsList.get(pointsList.size() - 1).getX(),
                            pointsList.get(pointsList.size() - 1).getX(),
                            t);
                    double y = computeInterpolation(pointsList.get(pointsList.size() - 2).getY(),
                            pointsList.get(pointsList.size() - 1).getY(),
                            pointsList.get(pointsList.size() - 1).getY(),
                            pointsList.get(pointsList.size() - 1).getY(),
                            t);
                    double z = computeInterpolation(pointsList.get(pointsList.size() - 2).getZ(),
                            pointsList.get(pointsList.size() - 1).getZ(),
                            pointsList.get(pointsList.size() - 1).getZ(),
                            pointsList.get(pointsList.size() - 1).getZ(),
                            t);
                    particleLocations.add(new Location(pointsList.get(0).getWorld(), x, y, z));
                }
                else if (segment == pointsList.size() - 2)
                {
                    double x = computeInterpolation(pointsList.get(pointsList.size() - 3).getX(),
                            pointsList.get(pointsList.size() - 2).getX(),
                            pointsList.get(pointsList.size() - 1).getX(),
                            pointsList.get(pointsList.size() - 1).getX(),
                            t);
                    double y = computeInterpolation(pointsList.get(pointsList.size() - 3).getY(),
                            pointsList.get(pointsList.size() - 2).getY(),
                            pointsList.get(pointsList.size() - 1).getY(),
                            pointsList.get(pointsList.size() - 1).getY(),
                            t);
                    double z = computeInterpolation(pointsList.get(pointsList.size() - 3).getZ(),
                            pointsList.get(pointsList.size() - 2).getZ(),
                            pointsList.get(pointsList.size() - 1).getZ(),
                            pointsList.get(pointsList.size() - 1).getZ(),
                            t);
                    particleLocations.add(new Location(pointsList.get(0).getWorld(), x, y, z));
                }
                else
                {
                    double x = computeInterpolation(pointsList.get(segment - 1).getX(),
                            pointsList.get(segment).getX(),
                            pointsList.get(segment + 1).getX(),
                            pointsList.get(segment + 2).getX(),
                            t);
                    double y = computeInterpolation(pointsList.get(segment - 1).getY(),
                            pointsList.get(segment).getY(),
                            pointsList.get(segment + 1).getY(),
                            pointsList.get(segment + 2).getY(),
                            t);
                    double z = computeInterpolation(pointsList.get(segment - 1).getZ(),
                            pointsList.get(segment).getZ(),
                            pointsList.get(segment + 1).getZ(),
                            pointsList.get(segment + 2).getZ(),
                            t);
                    particleLocations.add(new Location(pointsList.get(0).getWorld(), x, y, z));
                }
            }
        }
        return particleLocations;
    }

    private ArrayList<Location> getPoints()
    {
        if (_points.size() > 3)
        {
            return _points;
        }
        else if (_points.size() == 3)
        {
            ArrayList<Location> newPointsList = new ArrayList<>();
            newPointsList.add(_points.get(0));
            newPointsList.add(_points.get(1));
            newPointsList.add(_points.get(2));
            newPointsList.add(_points.get(2));
            return newPointsList;
        }
        else if (_points.size() == 2)
        {
            ArrayList<Location> newPointsList = new ArrayList<>();
            newPointsList.add(_points.get(0));
            newPointsList.add(_points.get(1));
            newPointsList.add(_points.get(1));
            newPointsList.add(_points.get(1));
            return newPointsList;
        }
        return null;
    }

    private double computeInterpolation(double p0, double p1, double p2, double p3, double t)
    {
        double deri = 0.5 * (p2 - p0);
        double derii = 0.5 * (p3 - p2);
        double a0 = p1;
        double a1 = deri;
        double a2 = 3 * (p2 - p1) - 2 * deri - derii;
        double a3 = deri + derii + 2 * (-p2 + p1);
        return a0 + a1 * t + a2 * t * t + a3 * t * t * t;
    }
}
