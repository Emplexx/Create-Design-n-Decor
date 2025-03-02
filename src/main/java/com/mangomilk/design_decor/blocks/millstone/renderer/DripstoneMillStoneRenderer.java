package com.mangomilk.design_decor.blocks.millstone.renderer;

import com.mangomilk.design_decor.registry.CDDPartialModels;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillstoneRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class DripstoneMillStoneRenderer extends MillstoneRenderer {

    public DripstoneMillStoneRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(MillstoneBlockEntity be, BlockState state) {
        return CachedBuffers.partial(CDDPartialModels.DRIPSTONE_MILLSTONE_COG, state);
    }
}
