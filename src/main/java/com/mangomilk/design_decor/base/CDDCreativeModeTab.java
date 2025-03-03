package com.mangomilk.design_decor.base;

import com.mangomilk.design_decor.DesignDecor;
import com.mangomilk.design_decor.registry.CDDBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.TagDependentIngredientItem;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.mutable.MutableObject;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CDDCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DesignDecor.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CDD_TAB = TABS.register("cdd_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.literal(DesignDecor.NAME))
                    .withTabsBefore(AllCreativeModeTabs.PALETTES_CREATIVE_TAB.getKey())
                    .icon(CDDBlocks.MOYAI_SIGN::asStack)
                    .displayItems(new RegistrateDisplayItemsGenerator(true, CDDCreativeModeTab.CDD_TAB))
                    .build()
    );

    private record RegistrateDisplayItemsGenerator(
            boolean addItems,
            RegistryObject<CreativeModeTab> tabFilter
    ) implements CreativeModeTab.DisplayItemsGenerator {
        private static final Predicate<Item> IS_ITEM_3D_PREDICATE;

        static {
            MutableObject<Predicate<Item>> isItem3d = new MutableObject<>(item -> false);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                isItem3d.setValue(item -> {
                    ItemRenderer itemRenderer = Minecraft.getInstance()
                            .getItemRenderer();
                    BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                    return model.isGui3d();
                });
            });
            IS_ITEM_3D_PREDICATE = isItem3d.getValue();
        }

        @OnlyIn(Dist.CLIENT)
        private static Predicate<Item> makeClient3dItemPredicate() {
            return item -> {
                ItemRenderer itemRenderer = Minecraft.getInstance()
                        .getItemRenderer();
                BakedModel model = itemRenderer.getModel(new ItemStack(item), null, null, 0);
                return model.isGui3d();
            };
        }

        private static Predicate<Item> makeExclusionPredicate() {
            Set<Item> exclusions = new ReferenceOpenHashSet<>();

            List<ItemProviderEntry<?>> simpleExclusions = List.of(
                    CDDBlocks.MMB_CRUSHING_WHEEL_CONTROLLER

                    //ITEMS LIST

            );

            List<ItemEntry<TagDependentIngredientItem>> tagDependentExclusions = List.of(
                    //CUSTOM ITEMS LIST
            );

            for (ItemProviderEntry<?> entry : simpleExclusions) {
                exclusions.add(entry.get().asItem());
            }

            for (ItemEntry<TagDependentIngredientItem> entry : tagDependentExclusions) {
                TagDependentIngredientItem item = entry.get();
                if (item.shouldHide()) {
                    exclusions.add(entry.get().asItem());
                }
            }

            return exclusions::contains;
        }

        private static List<RegistrateDisplayItemsGenerator.ItemOrdering> makeOrderings() {
            List<RegistrateDisplayItemsGenerator.ItemOrdering> orderings = new ReferenceArrayList<>();

            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleBeforeOrderings = Map.of(
            );

            Map<ItemProviderEntry<?>, ItemProviderEntry<?>> simpleAfterOrderings = Map.of(
            );

            simpleBeforeOrderings.forEach((entry, otherEntry) -> {
                orderings.add(RegistrateDisplayItemsGenerator.ItemOrdering.before(entry.get().asItem(), otherEntry.get().asItem()));
            });

            simpleAfterOrderings.forEach((entry, otherEntry) -> {
                orderings.add(RegistrateDisplayItemsGenerator.ItemOrdering.after(entry.get().asItem(), otherEntry.get().asItem()));
            });

            return orderings;
        }

        private static Function<Item, ItemStack> makeStackFunc() {
            Map<Item, Function<Item, ItemStack>> factories = new Reference2ReferenceOpenHashMap<>();

            Map<ItemProviderEntry<?>, Function<Item, ItemStack>> simpleFactories = Map.of(
            );

            simpleFactories.forEach((entry, factory) -> {
                factories.put(entry.get().asItem(), factory);
            });

            return item -> {
                Function<Item, ItemStack> factory = factories.get(item);
                if (factory != null) {
                    return factory.apply(item);
                }
                return new ItemStack(item);
            };
        }

        private static Function<Item, CreativeModeTab.TabVisibility> makeVisibilityFunc() {
            Map<Item, CreativeModeTab.TabVisibility> visibilities = new Reference2ObjectOpenHashMap<>();

            Map<ItemProviderEntry<?>, CreativeModeTab.TabVisibility> simpleVisibilities = Map.of(
            );

            simpleVisibilities.forEach((entry, factory) -> {
                visibilities.put(entry.get().asItem(), factory);
            });


            return item -> {
                CreativeModeTab.TabVisibility visibility = visibilities.get(item);
                if (visibility != null) {
                    return visibility;
                }
                return CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
            };
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            Predicate<Item> exclusionPredicate = makeExclusionPredicate();
            List<RegistrateDisplayItemsGenerator.ItemOrdering> orderings = makeOrderings();
            Function<Item, ItemStack> stackFunc = makeStackFunc();
            Function<Item, CreativeModeTab.TabVisibility> visibilityFunc = makeVisibilityFunc();

            List<Item> items = new LinkedList<>();
            if (addItems) {
                items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE.negate())));
            }
            items.addAll(collectBlocks(exclusionPredicate));
            if (addItems) {
                items.addAll(collectItems(exclusionPredicate.or(IS_ITEM_3D_PREDICATE)));
            }

            applyOrderings(items, orderings);
            outputAll(output, items, stackFunc, visibilityFunc);
        }

        private List<Item> collectBlocks(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : DesignDecor.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;
                Item item = entry.get().asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Item> entry : DesignDecor.REGISTRATE.getAll(Registries.ITEM)) {
                if (!CreateRegistrate.isInCreativeTab(entry, tabFilter))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void applyOrderings(List<Item> items, List<RegistrateDisplayItemsGenerator.ItemOrdering> orderings) {
            for (RegistrateDisplayItemsGenerator.ItemOrdering ordering : orderings) {
                int anchorIndex = items.indexOf(ordering.anchor());
                if (anchorIndex != -1) {
                    Item item = ordering.item();
                    int itemIndex = items.indexOf(item);
                    if (itemIndex != -1) {
                        items.remove(itemIndex);
                        if (itemIndex < anchorIndex) {
                            anchorIndex--;
                        }
                    }
                    if (ordering.type() == RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER) {
                        items.add(anchorIndex + 1, item);
                    } else {
                        items.add(anchorIndex, item);
                    }
                }
            }
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items, Function<Item, ItemStack> stackFunc, Function<Item, CreativeModeTab.TabVisibility> visibilityFunc) {
            for (Item item : items) {
                output.accept(stackFunc.apply(item), visibilityFunc.apply(item));
            }
        }

        private record ItemOrdering(Item item, Item anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type type) {
            public static RegistrateDisplayItemsGenerator.ItemOrdering before(Item item, Item anchor) {
                return new RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type.BEFORE);
            }

            public static RegistrateDisplayItemsGenerator.ItemOrdering after(Item item, Item anchor) {
                return new RegistrateDisplayItemsGenerator.ItemOrdering(item, anchor, RegistrateDisplayItemsGenerator.ItemOrdering.Type.AFTER);
            }

            public enum Type {
                BEFORE,
                AFTER;
            }
        }
    }

    // Leaving my unfinished manual labour here just in case
    private static void acceptItems(CreativeModeTab.Output output) {

        BlockEntry<?>[] blocks1 = new BlockEntry[]{
                CDDBlocks.BRASS_LAMP,
                CDDBlocks.COPPER_LAMP,
                CDDBlocks.ZINC_LAMP,
                CDDBlocks.BRASS_SCREW,
                CDDBlocks.COPPER_SCREW,
                CDDBlocks.ZINC_SCREW,
                CDDBlocks.BRASS_BOLT,
                CDDBlocks.COPPER_BOLT,
                CDDBlocks.ZINC_BOLT,
                CDDBlocks.BRASS_LIGHT,
                CDDBlocks.COPPER_LIGHT,
                CDDBlocks.ZINC_LIGHT,

                CDDBlocks.ANDESITE_FLOODLIGHT,
                CDDBlocks.BRASS_FLOODLIGHT,
                CDDBlocks.COPPER_FLOODLIGHT,

                CDDBlocks.COGWHEEL,
                CDDBlocks.LARGE_COGWHEEL,

                CDDBlocks.ANDESITE_BOILER,
                CDDBlocks.LARGE_ANDESITE_BOILER,
                CDDBlocks.BRASS_BOILER,
                CDDBlocks.LARGE_BRASS_BOILER,
                CDDBlocks.COPPER_BOILER,
                CDDBlocks.LARGE_COPPER_BOILER,
                CDDBlocks.ZINC_BOILER,
                CDDBlocks.LARGE_ZINC_BOILER,
                CDDBlocks.INDUSTRIAL_IRON_BOILER,
                CDDBlocks.LARGE_INDUSTRIAL_IRON_BOILER,
                CDDBlocks.CAST_IRON_BOILER,
                CDDBlocks.LARGE_CAST_IRON_BOILER,
                CDDBlocks.GOLD_BOILER,
                CDDBlocks.LARGE_GOLD_BOILER,
                CDDBlocks.CAPITALISM_BOILER,
                CDDBlocks.LARGE_CAPITALISM_BOILER,
                CDDBlocks.ALUMINUM_BOILER,
                CDDBlocks.ALUMINUM_BOILER_SPECIAL,
                CDDBlocks.LARGE_ALUMINUM_BOILER,

                CDDBlocks.WOOD_SUPPORT,
                CDDBlocks.METAL_SUPPORT,
                CDDBlocks.DIAGONAL_METAL_SUPPORT,
                CDDBlocks.DIAGONAL_GIRDER,

                CDDBlocks.GAS_TANK,
                CDDBlocks.COPPER_GAS_TANK,

                CDDBlocks.RED_CONTAINER,
                CDDBlocks.GREEN_CONTAINER,
                CDDBlocks.BLUE_CONTAINER,

                CDDBlocks.WARNING_SIGN,
                CDDBlocks.STOP_SIGN,
                CDDBlocks.ARROW_UP_SIGN,
                CDDBlocks.ARROW_DOWN_SIGN,
                CDDBlocks.ARROW_LEFT_SIGN,
                CDDBlocks.ARROW_RIGHT_SIGN,

                CDDBlocks.MOYAI_SIGN,
                CDDBlocks.TAP_SIGN,
                CDDBlocks.GLITCH_WARNING_SIGN,
                CDDBlocks.BROKEN_WRENCH_SIGN,
                CDDBlocks.BIOHAZARD_SIGN,
                CDDBlocks.CAPITALISM_WARNING_SIGN,
                CDDBlocks.GEAR_SIGN,
                CDDBlocks.CREEPER_SIGN,
                CDDBlocks.BUN_SIGN,
                CDDBlocks.SILLY_SIGN,
                CDDBlocks.OIL_SIGN,
                CDDBlocks.MAGNET_SIGN,

                CDDBlocks.BLANK_SIGN,
        };

        BlockEntry<?>[] blocks2 = new BlockEntry[]{
                CDDBlocks.LETTER_SIGN,

                CDDBlocks.GRANITE_CRUSHING_WHEEL,
                CDDBlocks.DIORITE_CRUSHING_WHEEL,
                CDDBlocks.LIMESTONE_CRUSHING_WHEEL,
                CDDBlocks.OCHRUM_CRUSHING_WHEEL,
                CDDBlocks.SCORCHIA_CRUSHING_WHEEL,
                CDDBlocks.SCORIA_CRUSHING_WHEEL,
                CDDBlocks.TUFF_CRUSHING_WHEEL,
                CDDBlocks.VERIDIUM_CRUSHING_WHEEL,
                CDDBlocks.DRIPSTONE_CRUSHING_WHEEL,
                CDDBlocks.DEEPSLATE_CRUSHING_WHEEL,
                CDDBlocks.CRIMSITE_CRUSHING_WHEEL,
                CDDBlocks.CALCITE_CRUSHING_WHEEL,
                CDDBlocks.ASURINE_CRUSHING_WHEEL,

                CDDBlocks.GRANITE_MILLSTONE,
                CDDBlocks.DIORITE_MILLSTONE,
                CDDBlocks.LIMESTONE_MILLSTONE,
                CDDBlocks.OCHRUM_MILLSTONE,
                CDDBlocks.SCORCHIA_MILLSTONE,
                CDDBlocks.SCORIA_MILLSTONE,
                CDDBlocks.TUFF_MILLSTONE,
                CDDBlocks.VERIDIUM_MILLSTONE,
                CDDBlocks.DRIPSTONE_MILLSTONE,
                CDDBlocks.DEEPSLATE_MILLSTONE,
                CDDBlocks.CRIMSITE_MILLSTONE,
                CDDBlocks.CALCITE_MILLSTONE,
                CDDBlocks.ASURINE_MILLSTONE,

                CDDBlocks.CAPITALISM_BLOCK,
                CDDBlocks.INDUSTRIAL_GOLD_BLOCK,
                CDDBlocks.CARDBOARD_BOX,

                CDDBlocks.BRASS_FLOOR,
                CDDBlocks.COPPER_FLOOR,
                CDDBlocks.ZINC_FLOOR,
                CDDBlocks.INDUSTRIAL_IRON_FLOOR,
                CDDBlocks.INDUSTRIAL_GOLD_FLOOR,
                CDDBlocks.INDUSTRIAL_PLATING_BLOCK,
                CDDBlocks.STONE_TILES,
                CDDBlocks.RED_STONE_TILES
        };


    }
}
