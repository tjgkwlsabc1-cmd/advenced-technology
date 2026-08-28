package advtech;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

import mindustry.gen.Building;
import mindustry.game.Team;
import mindustry.world.Tile;
import mindustry.world.blocks.production.Pump;

public class PressurePump extends Pump {
    public float pressureIncrease = 10f;
    public int maxPipelines = 15;

    public PressurePump(String name) {
        super(name);
        buildType = PressurePumpBuild::new;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return true;
    }

    public class PressurePumpBuild extends PumpBuild {
        @Override
        public void updateTile() {
            super.updateTile();
            distributePressure();
        }

        private void distributePressure() {
            ArrayDeque<Building> pending = new ArrayDeque<>();
            HashSet<Building> visited = new HashSet<>();
            List<PressureConduit.PressureConduitBuild> pipes = new ArrayList<>();
            pending.add(this);

            int pumpCount = 0;
            while (!pending.isEmpty() && pipes.size() < maxPipelines) {
                Building current = pending.removeFirst();
                if (!visited.add(current)) continue;
                if (current instanceof PressurePumpBuild) pumpCount++;

                for (Building next : current.proximity) {
                    if (next == null || visited.contains(next)) continue;
                    if (next instanceof PressureConduit.PressureConduitBuild pipe) {
                        pipes.add(pipe);
                        pending.addLast(pipe);
                    } else if (next instanceof PressurePumpBuild) {
                        pending.addLast(next);
                    }
                }
            }

            float pressure = pumpCount * pressureIncrease;
            for (PressureConduit.PressureConduitBuild pipe : pipes) {
                pipe.setPressure(pressure);
            }
        }
    }
}