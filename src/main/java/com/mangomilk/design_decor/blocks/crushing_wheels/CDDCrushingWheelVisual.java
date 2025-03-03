package com.mangomilk.design_decor.blocks.crushing_wheels;

import com.mangomilk.design_decor.registry.CDDBlocks;
import com.mangomilk.design_decor.registry.CDDPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.world.level.block.state.BlockState;

public class CDDCrushingWheelVisual {

    public static SingleAxisRotatingVisual<MmbCrushingWheelBlockEntity> create(
            VisualizationContext context, MmbCrushingWheelBlockEntity blockEntity, float partialTick
    ) {

        BlockState b = blockEntity.getBlockState();
        PartialModel m = AllPartialModels.CRUSHING_WHEEL;

        // kill me
        if (CDDBlocks.GRANITE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.GRANITE_CW;
        if (CDDBlocks.DIORITE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.DIORITE_CW;
        if (CDDBlocks.LIMESTONE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.LIMESTONE_CW;
        if (CDDBlocks.SCORCHIA_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.SCORCHIA_CW;
        if (CDDBlocks.SCORIA_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.SCORIA_CW;
        if (CDDBlocks.TUFF_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.TUFF_CW;
        if (CDDBlocks.VERIDIUM_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.VERIDIUM_CW;
        if (CDDBlocks.DRIPSTONE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.DRIPSTONE_CW;
        if (CDDBlocks.DEEPSLATE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.DEEPSLATE_CW;
        if (CDDBlocks.CRIMSITE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.CRIMSITE_CW;
        if (CDDBlocks.CALCITE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.CALCITE_CW;
        if (CDDBlocks.ASURINE_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.ASURINE_CW;
        if (CDDBlocks.OCHRUM_CRUSHING_WHEEL.has(b)) m = CDDPartialModels.OCHRUM_CW;

        return new SingleAxisRotatingVisual<MmbCrushingWheelBlockEntity>(context, blockEntity, partialTick, Models.partial(m));
    }
}
