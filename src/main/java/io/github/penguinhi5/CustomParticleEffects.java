/*
 * MIT License
 *
 * Copyright (c) 2023 PenguinHi5
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to no conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.penguinhi5;

import io.github.penguinhi5.command.ParticleEffectCmd;
import io.github.penguinhi5.effect.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class CustomParticleEffects extends JavaPlugin
{
    private HashMap<String, ParticleEffect> _particleEffects;

    public void onEnable()
    {
        initializeParticleEffects();

        getCommand("cpe").setExecutor(new ParticleEffectCmd(this, _particleEffects));
    }

    private void initializeParticleEffects()
    {
        // particle effects must be added here
        _particleEffects = new HashMap<>();
        _particleEffects.put("line", new LineParticleEffect(this));
        _particleEffects.put("multiline", new MultiLineParticleEffect(this));
        _particleEffects.put("curvedline", new CurvedLineParticleEffect(this));
    }

    public void stopParticles()
    {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Stopping all particle effects");
        for (ParticleEffect effect : _particleEffects.values())
        {
            effect.stop();
        }
    }

}
