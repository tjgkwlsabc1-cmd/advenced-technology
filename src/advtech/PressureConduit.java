package advtech;

import arc.struct.Seq;
import arc.graphics.Color;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Tile;
import mindustry.world.blocks.liquid.ArmoredConduit;
import mindustry.world.blocks.liquid.LiquidBridge;
import mindustry.world.blocks.liquid.LiquidJunction;
import mindustry.world.blocks.liquid.LiquidRouter;

public class PressureConduit extends ArmoredConduit {
    public float minPressure;
    public float maxPressure;
    public float speedIncrease;

    public PressureConduit(String name, float minPressure, float maxPressure, float speedIncrease) {
        super(name);
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
        this.speedIncrease = speedIncrease;
        buildType = PressureConduitBuild::new;
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, mindustry.world.Block otherblock) {
        if (otherblock instanceof PressureConduit) {
            return super.blends(tile, rotation, otherx, othery, otherrot, otherblock);
        }
        return (otherblock instanceof LiquidJunction || otherblock instanceof LiquidBridge || otherblock instanceof LiquidRouter)
            && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    public class PressureConduitBuild extends ArmoredConduitBuild {
        public float pressure;

        public PressureConduitBuild() {
            super();
        }

        @Override
        public boolean acceptLiquid(Building source, mindustry.type.Liquid liquid) {
            return pressure >= minPressure && super.acceptLiquid(source, liquid);
        }

        @Override
        public void updateTile() {
            if (pressure > maxPressure) {
                damageContinuous((pressure - maxPressure) * 0.02f);
            }
            if (pressure >= minPressure) {
                super.updateTile();
            } else {
                noSleep();
            }
        }

        public void setPressure(float value) {
            pressure = value;
        }

        public Seq<Building> connectedBuildings() {
            return proximity;
        }
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("pressure", (PressureConduitBuild build) -> new Bar(
            () -> "Pressure: " + (int)build.pressure + " / " + (int)maxPressure,
            () -> pressureColor(build.pressure),
            () -> Math.min(1f, Math.max(0f, build.pressure / maxPressure))
        ));
    }

    private Color pressureColor(float pressure) {
        float ratio = Math.min(1f, Math.max(0f, pressure / maxPressure));
        if (ratio < 0.5f) {
            return new Color(Color.green).lerp(Color.yellow, ratio * 2f);
        }
        return new Color(Color.yellow).lerp(Color.red, (ratio - 0.5f) * 2f);
    }
}