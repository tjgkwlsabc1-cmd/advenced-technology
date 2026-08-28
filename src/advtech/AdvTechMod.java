package advtech;

import mindustry.mod.Mod;
import mindustry.content.Blocks;
import mindustry.content.TechTree;
import mindustry.type.ItemStack;
import arc.Events;
import arc.util.Log;
import mindustry.game.EventType.ClientLoadEvent;

public class AdvTechMod extends Mod {
    private boolean techTreeConfigured;

    public AdvTechMod() {
        // Reconfigure the tech tree after all game client resources are fully loaded.
        Events.on(ClientLoadEvent.class, event -> {
            setupTechTree();
        });
    }

    @Override
    public void loadContent() {
        // Load all custom items and blocks
        AdvTechContent.load();
        Log.info("[Advanced Technology] Content loaded successfully!");
    }

    private void setupTechTree() {
        if (techTreeConfigured) return;

        try {
            // 1. Setup Plate Drill in Tech Tree (Under Mechanical Drill)
            TechTree.TechNode mechanicalNode = Blocks.mechanicalDrill.techNode;
            if (mechanicalNode != null) {
                // Research cost is double the construction requirements.
                TechTree.TechNode plateDrillNode = TechTree.node(AdvTechContent.plateDrill, ItemStack.with(
                    mindustry.content.Items.copper, 30,
                    mindustry.content.Items.lead, 20,
                    AdvTechContent.leadCopperPlate, 10
                ), () -> {});

                // Move vanilla pneumatic drill to be a child of Plate Drill
                TechTree.TechNode pneumaticNode = Blocks.pneumaticDrill.techNode;
                if (pneumaticNode != null && pneumaticNode.parent != null) {
                    pneumaticNode.parent.children.remove(pneumaticNode);
                    pneumaticNode.parent = plateDrillNode;
                    plateDrillNode.children.add(pneumaticNode);
                }
            }

            // 2. Setup Plated Conduit before the vanilla Conduit
            TechTree.TechNode vanillaConduitNode = Blocks.conduit.techNode;
            if (vanillaConduitNode != null && vanillaConduitNode.parent != null) {
                TechTree.TechNode conduitParent = vanillaConduitNode.parent;
                TechTree.TechNode platedConduitNode = TechTree.node(AdvTechContent.platedConduit, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 400,
                    mindustry.content.Items.lead, 500
                ), () -> {});

                conduitParent.children.remove(vanillaConduitNode);
                if (!conduitParent.children.contains(platedConduitNode)) {
                    conduitParent.children.add(platedConduitNode);
                }
                platedConduitNode.parent = conduitParent;
                if (!platedConduitNode.children.contains(vanillaConduitNode)) {
                    platedConduitNode.children.add(vanillaConduitNode);
                }
                vanillaConduitNode.parent = platedConduitNode;

                TechTree.TechNode pressureConduitNode = TechTree.node(AdvTechContent.pressureConduit, ItemStack.with(
                    mindustry.content.Items.lead, 10,
                    mindustry.content.Items.metaglass, 10
                ), () -> {});
                conduitParent.children.add(pressureConduitNode);
                pressureConduitNode.parent = conduitParent;

                TechTree.TechNode pressurePulseConduitNode = TechTree.node(AdvTechContent.pressurePulseConduit, ItemStack.with(
                    mindustry.content.Items.lead, 20,
                    mindustry.content.Items.metaglass, 20
                ), () -> {});
                pressureConduitNode.children.add(pressurePulseConduitNode);
                pressurePulseConduitNode.parent = pressureConduitNode;

                TechTree.TechNode platedConduitBridgeNode = TechTree.node(AdvTechContent.platedConduitBridge, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 20,
                    mindustry.content.Items.lead, 40
                ), () -> {});
                platedConduitNode.children.add(platedConduitBridgeNode);
                platedConduitBridgeNode.parent = platedConduitNode;
            }

            if (vanillaConduitNode != null) {
                TechTree.TechNode platedRouterNode = TechTree.node(AdvTechContent.platedRouter, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 400,
                    mindustry.content.Items.lead, 500
                ), () -> {});

                if (!vanillaConduitNode.children.contains(platedRouterNode)) {
                    vanillaConduitNode.children.add(platedRouterNode);
                }
                platedRouterNode.parent = vanillaConduitNode;
            }


            if (vanillaConduitNode != null) {
                TechTree.TechNode platedLiquidJunctionNode = TechTree.node(AdvTechContent.platedLiquidJunction, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 400,
                    mindustry.content.Items.lead, 500
                ), () -> {});

                if (!vanillaConduitNode.children.contains(platedLiquidJunctionNode)) {
                    vanillaConduitNode.children.add(platedLiquidJunctionNode);
                }
                platedLiquidJunctionNode.parent = vanillaConduitNode;
            }

            TechTree.TechNode steamPressNode = TechTree.node(AdvTechContent.steamPress, ItemStack.with(
                AdvTechContent.leadCopperPlate, 40,
                mindustry.content.Items.copper, 100,
                mindustry.content.Items.lead, 60
            ), () -> {});

            TechTree.TechNode graphitePressNode = Blocks.graphitePress.techNode;
            if (graphitePressNode != null && graphitePressNode.parent != null) {
                TechTree.TechNode graphiteParent = graphitePressNode.parent;

                graphiteParent.children.remove(graphitePressNode);
                if (!graphiteParent.children.contains(steamPressNode)) {
                    graphiteParent.children.add(steamPressNode);
                }
                steamPressNode.parent = graphiteParent;
                if (!steamPressNode.children.contains(graphitePressNode)) {
                    steamPressNode.children.add(graphitePressNode);
                }
                graphitePressNode.parent = steamPressNode;
            }

            TechTree.TechNode pumpNode = Blocks.mechanicalPump.techNode;
            if (pumpNode != null && pumpNode.parent != null) {
                TechTree.TechNode pumpParent = pumpNode.parent;
                TechTree.TechNode platedPumpNode = TechTree.node(AdvTechContent.platedPump, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 40,
                    mindustry.content.Items.copper, 100,
                    mindustry.content.Items.lead, 60
                ), () -> {});

                pumpParent.children.remove(pumpNode);
                if (!pumpParent.children.contains(platedPumpNode)) {
                    pumpParent.children.add(platedPumpNode);
                }
                platedPumpNode.parent = pumpParent;
                if (!platedPumpNode.children.contains(pumpNode)) {
                    platedPumpNode.children.add(pumpNode);
                }
                pumpNode.parent = platedPumpNode;
            }

            TechTree.TechNode mechanicalPumpNode = Blocks.mechanicalPump.techNode;
            if (mechanicalPumpNode != null && mechanicalPumpNode.parent != null) {
                TechTree.TechNode airPumpParent = mechanicalPumpNode.parent;
                TechTree.TechNode mechanicalAirPumpNode = TechTree.node(AdvTechContent.mechanicalAirPump, ItemStack.with(
                    AdvTechContent.leadCopperPlate, 1000,
                    mindustry.content.Items.lead, 1500
                ), () -> {});

                airPumpParent.children.remove(mechanicalPumpNode);
                if (!airPumpParent.children.contains(mechanicalAirPumpNode)) {
                    airPumpParent.children.add(mechanicalAirPumpNode);
                }
                mechanicalAirPumpNode.parent = airPumpParent;
                if (!mechanicalAirPumpNode.children.contains(mechanicalPumpNode)) {
                    mechanicalAirPumpNode.children.add(mechanicalPumpNode);
                }
                mechanicalPumpNode.parent = mechanicalAirPumpNode;
            }

            techTreeConfigured = true;
            Log.info("[Advanced Technology] Tech tree successfully reconfigured!");
        } catch (Exception e) {
            Log.err("[Advanced Technology] Failed to reconfigure tech tree", e);
        }
    }
}

