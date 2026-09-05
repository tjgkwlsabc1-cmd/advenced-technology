package advtech;

import mindustry.entities.bullet.BulletType;
import arc.graphics.Color;

public class LightMissile extends BulletType {
    public LightMissile() {
        super(10f, 100f); // speed=10, damage=100
        
        lifetime = 24f; // 30 blocks * 8 tiles/block / 10 tiles/sec = 24 sec
        buildingDamageMultiplier = 0.01f; // -99% building damage
        
        // Trail configuration: yellow trail
        trailColor = Color.valueOf("fffd38"); // yellow
        trailChance = 1f; // always create trail
        trailInterval = 2f; // trail every 2 ticks
        trailLength = 8; // trail length
        trailWidth = 2f; // trail width
        
        // Visual properties
        hitSize = 4f;
        splashDamage = 0f; // no splash damage
        splashDamageRadius = 5f; // 5 block explosion radius
        speed = 10f;
        
        // Knockback
        knockback = 0.5f;
    }
}
