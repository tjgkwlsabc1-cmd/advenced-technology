package advtech;

import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.Drill;
import mindustry.content.Blocks;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.production.Pump;
import mindustry.world.blocks.liquid.LiquidBridge;
import mindustry.world.blocks.liquid.LiquidJunction;
import mindustry.world.blocks.liquid.LiquidRouter;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.content.Items;
import mindustry.entities.Effect;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;

public class AdvTechContent {
    public static Item leadCopperPlate;
    public static Liquid steam;
    public static Block plateDrill;
    public static Block platedConduit;
    public static Block pressureConduit;
    public static Block pressurePulseConduit;
    public static Block platedRouter;
    public static Block platedLiquidJunction;
    public static LiquidBridge platedConduitBridge;
    public static Block steamPress;
    public static Block platedPump;
    public static Block mechanicalAirPump;

    public static void load() {
        // Custom Resource: Lead-Copper Plate
        leadCopperPlate = new Item("lead-copper-plate", Color.valueOf("cca37a")) {{
            localizedName = "Lead-Copper Plate";
            description = "Sealed copper plate. useful";
            cost = 1.5f;
        }};

        steam = new Liquid("steam", Color.valueOf("ebeef5")) {{
            localizedName = "Steam";
            description = "High-temperature steam used for advanced processing.";
            temperature = 2.0f;
            gas = true;
            viscosity = 0.25f;
            heatCapacity = 0.6f;
        }};

        // Custom Block: Plate Drill
        plateDrill = new Drill("plate-drill") {{
            localizedName = "Plate Drill";
            description = "Faster than a mechanical drill. Mining efficiency can be significantly boosted (2.5x) when supplied with water. Can mine up to coal.";
            
            size = 2; 
            tier = 2;          
            drillTime = 220f;  

            hasLiquids = true;
            liquidCapacity = 5f;
            liquidBoostIntensity = 2.5f; 

            hasPower = false; 

            requirements(Category.production, ItemStack.with(
                Items.copper, 15,
                Items.lead, 10,
                leadCopperPlate, 5
            ));
        }};

        pressureConduit = createPressureConduit("pressure-conduit", "conduit", 10f, 300f, 3f);
        pressurePulseConduit = createPressureConduit("pressure-pulse-conduit", "pulse-conduit", 100f, 500f, 5f);
        pressureConduit.localizedName = "Pressure Conduit";
        pressureConduit.description = "A pressure-controlled liquid conduit.";
        pressureConduit.requirements(Category.liquid, ItemStack.with(Items.lead, 1, Items.metaglass, 1));
        pressurePulseConduit.localizedName = "Pressure Pulse Conduit";
        pressurePulseConduit.description = "A high-pressure liquid conduit.";
        pressurePulseConduit.requirements(Category.liquid, ItemStack.with(Items.lead, 2, Items.metaglass, 2));
        addPressureStats(pressureConduit, 10, 300, 3);
        addPressureStats(pressurePulseConduit, 100, 500, 5);
        Blocks.conduit.buildVisibility = mindustry.world.meta.BuildVisibility.hidden;
        Blocks.pulseConduit.buildVisibility = mindustry.world.meta.BuildVisibility.hidden;
        Blocks.platedConduit.buildVisibility = mindustry.world.meta.BuildVisibility.hidden;

        // Custom Block: Plated Conduit
        platedConduit = new PressureConduit("pressure-plated-conduit", 150f, 1000f, 2f) {
            @Override
            public void load() {
                super.load();
                
                arc.graphics.g2d.TextureRegion customTop = arc.Core.atlas.find("plated-conduit-top");
                arc.graphics.g2d.TextureRegion customL = arc.Core.atlas.find("plated-conduit-top-L");
                arc.graphics.g2d.TextureRegion customT = arc.Core.atlas.find("plated-conduit-top-t");
                arc.graphics.g2d.TextureRegion customX = arc.Core.atlas.find("plated-conduit-top-x");
                region = arc.Core.atlas.find("plated-conduit-base", region);
                capRegion = arc.Core.atlas.find("plated-conduit-edge", capRegion);

                if (topRegions != null) {
                    if (customTop.found() && topRegions.length > 0) topRegions[0] = customTop;
                    if (customL.found() && topRegions.length > 1) topRegions[1] = customL;
                    if (customT.found() && topRegions.length > 2) topRegions[2] = customT;
                    if (customX.found() && topRegions.length > 3) topRegions[3] = customX;
                    if (customTop.found() && topRegions.length > 4) topRegions[4] = customTop;
                }
                if (botRegions != null) {
                    for (int index = 0; index < botRegions.length; index++) {
                        botRegions[index] = arc.Core.atlas.find("plated-conduit-bottom", botRegions[index]);
                    }
                }
            }
        };

        // Block settings for Plated Conduit
        platedConduit.localizedName = "Plated Conduit";
        platedConduit.description = "A specialized pipe. Low capacity, but high liquid pressure ensures ultra-fast flow speed.";
        
        platedConduit.liquidCapacity = 10f;    
        platedConduit.liquidPressure = 3.0f;   
        platedConduit.health = 140;

        // Building Requirements: 1 Lead-Copper Plate, 2 Lead
        platedConduit.requirements(Category.liquid, ItemStack.with(
            leadCopperPlate, 1,
            Items.lead, 2
        ));
        addPressureStats(platedConduit, 70, 1000, 4);

        platedRouter = new LiquidRouter("plated-router") {
            @Override
            public void load() {
                super.load();
                arc.graphics.g2d.TextureRegion customRegion = arc.Core.atlas.find("plater-conduit-router", region);
                region = customRegion;
                topRegion = customRegion;
                bottomRegion = customRegion;
            }
        };

        platedRouter.localizedName = "Plated Router";
        platedRouter.description = "Distributes liquids to adjacent pipes at high pressure.";
        platedRouter.liquidCapacity = 10f;
        platedRouter.liquidPressure = 3.0f;
        platedRouter.health = 600;
        platedRouter.requirements(Category.liquid, ItemStack.with(
            leadCopperPlate, 2,
            Items.lead, 4
        ));
        addConnectedPressureStats(platedRouter);

        platedLiquidJunction = new LiquidJunction("plated-liquid-junction") {
            @Override
            public void load() {
                super.load();
                arc.graphics.g2d.TextureRegion customRegion = arc.Core.atlas.find("plated-conduit-Ju", region);
                region = customRegion;
                topRegion = customRegion;
                bottomRegion = customRegion;
            }
        };

        platedLiquidJunction.localizedName = "Plated Liquid Junction";
        platedLiquidJunction.description = "Connects liquid pipes with improved flow speed.";
        platedLiquidJunction.liquidCapacity = 10f;
        platedLiquidJunction.liquidPressure = 3.5f;
        platedLiquidJunction.health = 600;
        platedLiquidJunction.requirements(Category.liquid, ItemStack.with(
            leadCopperPlate, 2,
            Items.lead, 4
        ));
        addConnectedPressureStats(platedLiquidJunction);

        platedConduitBridge = new LiquidBridge("plated-conduit-bridge") {
            @Override
            public void load() {
                super.load();
                arc.graphics.g2d.TextureRegion customRegion = arc.Core.atlas.find("plated-conduit-bridge", region);
                region = customRegion;
                endRegion = customRegion;
                bridgeRegion = customRegion;
            }
        };

        platedConduitBridge.localizedName = "Plated Conduit Bridge";
        platedConduitBridge.description = "Transfers liquids across gaps up to 3 tiles.";
        platedConduitBridge.range = 3;
        platedConduitBridge.health = 700;
        platedConduitBridge.requirements(Category.liquid, ItemStack.with(
            leadCopperPlate, 10,
            Items.lead, 20
        ));

        steamPress = new GenericCrafter("steam-press") {{
            localizedName = "Steam Press";
            description = "Compresses steam and coal into graphite.";
            size = 2;
            health = 500;
            craftTime = 60f;
            outputItem = new ItemStack(Items.graphite, 3);
            consumeItems(ItemStack.with(Items.coal, 4));
            consumeLiquid(steam, 30f);
            drawer = new mindustry.world.draw.DrawBlock() {
                private final arc.graphics.g2d.TextureRegion[] frames = new arc.graphics.g2d.TextureRegion[6];

                @Override
                public void load(Block block) {
                    frames[0] = arc.Core.atlas.find("steam-press-an1");
                    frames[1] = arc.Core.atlas.find("steam-press-an2");
                    frames[2] = arc.Core.atlas.find("steam-press-an3");
                    frames[3] = frames[1];
                    frames[4] = frames[0];
                    frames[5] = arc.Core.atlas.find("steam-press");
                }

                @Override
                public void draw(mindustry.gen.Building build) {
                    int frame = Math.min(5, (int)(build.progress() * frames.length));
                    arc.graphics.g2d.Draw.rect(frames[frame], build.x, build.y, build.rotdeg());
                }

                @Override
                public arc.graphics.g2d.TextureRegion[] icons(Block block) {
                    return new arc.graphics.g2d.TextureRegion[]{frames[5]};
                }
            };
            craftEffect = new Effect(120f, e -> {
                Draw.color(Color.white, steam.color, e.fout());
                Fill.circle(e.x, e.y + e.fin() * 8f, 5f + e.fin() * 6f);
                Fill.circle(e.x - 5f, e.y + 3f + e.fin() * 5f, 3f + e.fin() * 4f);
                Fill.circle(e.x + 5f, e.y + 3f + e.fin() * 6f, 3f + e.fin() * 4f);
                Draw.reset();
            });
            requirements(Category.production, ItemStack.with(
                leadCopperPlate, 20,
                Items.copper, 50,
                Items.lead, 30
            ));
        }};

        platedPump = new Pump("plated-pump") {{
            localizedName = "Plated Pump";
            description = "Pumps liquid at a high rate.";
            size = 2;
            pumpAmount = 0.25f;
            liquidCapacity = 20f;
            health = 1000;
            requirements(Category.liquid, ItemStack.with(
                leadCopperPlate, 20,
                Items.copper, 50,
                Items.lead, 30
            ));
        }};
        addConnectedPressureStats(platedPump);

        mechanicalAirPump = new PressurePump("mechanical-air-pump") {
            @Override
            public void load() {
                super.load();
                region = arc.Core.atlas.find("mec-air-pump", region);
            }

            {
            localizedName = "Mechanical Air Pump";
            description = "Pumps liquid into a connected pipeline and increases its pressure.";
            size = 1;
            pumpAmount = 0.5f;
            liquidCapacity = 20f;
            requirements(Category.liquid, ItemStack.with(
                leadCopperPlate, 100,
                Items.lead, 200,
                Items.copper, 100
            ));
            }
        };
        mechanicalAirPump.stats.add(new mindustry.world.meta.Stat("pressure-increase", mindustry.world.meta.StatCat.liquids), "+10");
        mechanicalAirPump.stats.add(new mindustry.world.meta.Stat("pipeline-limit", mindustry.world.meta.StatCat.liquids), "15");
    }

    private static Block createPressureConduit(String name, String vanillaName, float minPressure, float maxPressure, float speedIncrease) {
        return new PressureConduit(name, minPressure, maxPressure, speedIncrease) {
            @Override
            public void load() {
                super.load();
                Conduit source = vanillaName.equals("pulse-conduit") ? (Conduit)Blocks.pulseConduit : (Conduit)Blocks.conduit;
                if (source.topRegions != null) {
                    if (topRegions == null || topRegions.length != source.topRegions.length) {
                        topRegions = new arc.graphics.g2d.TextureRegion[source.topRegions.length];
                    }
                    java.lang.System.arraycopy(source.topRegions, 0, topRegions, 0, source.topRegions.length);
                }
                if (source.botRegions != null) {
                    if (botRegions == null || botRegions.length != source.botRegions.length) {
                        botRegions = new arc.graphics.g2d.TextureRegion[source.botRegions.length];
                    }
                    java.lang.System.arraycopy(source.botRegions, 0, botRegions, 0, source.botRegions.length);
                }
                if (source.capRegion != null) capRegion = source.capRegion;
                if (source.region != null) region = source.region;
            }
        };
    }

    private static void addPressureStats(Block block, int minPressure, int maxPressure, int speedIncrease) {
        block.stats.add(new mindustry.world.meta.Stat("min-pressure", mindustry.world.meta.StatCat.liquids), String.valueOf(minPressure));
        block.stats.add(new mindustry.world.meta.Stat("max-pressure", mindustry.world.meta.StatCat.liquids), String.valueOf(maxPressure));
        block.stats.add(new mindustry.world.meta.Stat("pressure-speed", mindustry.world.meta.StatCat.liquids), "+" + speedIncrease);
    }

    private static void addConnectedPressureStats(Block block) {
        block.stats.add(new mindustry.world.meta.Stat("pressure-behavior", mindustry.world.meta.StatCat.liquids), "Uses connected pipe pressure");
    }
}
