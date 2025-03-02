package com.mangomilk.design_decor.base;

import com.mangomilk.design_decor.DesignDecor;
import com.mangomilk.design_decor.registry.CDDBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CDDCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
            .create(Registries.CREATIVE_MODE_TAB, DesignDecor.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> CDD_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> CDD_TAB = TABS.register(DesignDecor.MOD_ID,
            () -> CreativeModeTab.builder()
                    .title(Component.literal("Create: Design n' Decor"))
                    .icon(CDDBlocks.MOYAI_SIGN::asStack)
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(CDDBlocks.ARROW_LEFT_SIGN);
                        output.accept(CDDBlocks.ANDESITE_FLOODLIGHT);
                    })
                    .build()
    );

    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        CDD_TAB_ITEMS.add(itemLike);
        return itemLike;
    }

}
