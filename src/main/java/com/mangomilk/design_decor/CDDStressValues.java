package com.mangomilk.design_decor;

import com.simibubi.create.api.stress.BlockStressValues;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.DoubleSupplier;

public class CDDStressValues {

    private static final Object2DoubleMap<ResourceLocation> CDD_IMPACTS = new Object2DoubleOpenHashMap();

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setNoImpact() {
        return setImpact(0.0);
    }

    public static <B extends Block, P> NonNullUnaryOperator<BlockBuilder<B, P>> setImpact(double impact) {
        return (builder) -> {
            ResourceLocation id = DesignDecor.asResource(builder.getName());
            CDD_IMPACTS.put(id, impact);
            return builder;
        };
    }

    public static @Nullable DoubleSupplier getImpact(Block block) {

        ResourceLocation id = CatnipServices.REGISTRIES.getKeyOrThrow(block);
        double value = CDD_IMPACTS.getOrDefault(id, Double.MIN_VALUE);

        DoubleSupplier supplier;
        if (value == Double.MIN_VALUE) supplier = null;
        else supplier = () -> value;

        return supplier;
    }
}
