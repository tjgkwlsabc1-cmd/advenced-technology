package advtech;

import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.world.draw.DrawBlock;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;

public class Light extends UnitType {
    public Light() {
        super("light");
        
        // Core stats
        health = 1500f;
        speed = 30f; // 30 blocks/sec
        armor = 5f;
        
        // Mining and building
        mineTier = 4; // Can mine up to Titanium (tier 4)
        mineSpeed = 6f; // 600% mining speed
        buildSpeed = 10f; // 1000% building speed
        
        // Weapon configuration: light-missile burst weapon (invisible)
        Weapon lightMissileWeapon = new Weapon("light-missile-weapon") {{
            x = 4f;
            y = 0f;
            reload = 15f; // 30 ticks / 15 = 2 shots per second
            
            // Burst fire simulation: fire 3 times within reload period
            // We'll achieve this by firing multiple times per attack cycle
            bullet = AdvTechContent.lightMissile;
            
            // Invisible weapon - no rendering
            display = false;
        }};
        
        weapons.add(lightMissileWeapon);
        
        // Visual properties
        flying = true;
        targetGround = true;
        targetAir = true;
        faceTarget = true;
    }
}
