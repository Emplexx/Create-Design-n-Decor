package com.mangomilk.design_decor.blocks.large_boiler.andesite;

import com.mangomilk.design_decor.blocks.large_boiler.aluminum.AluminumLargeBoilerBlock;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.data.Pair;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AndesiteLargeBoilerBlockItem extends BlockItem {

    public AndesiteLargeBoilerBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public InteractionResult place(BlockPlaceContext ctx) {
        InteractionResult result = super.place(ctx);
        if (result != InteractionResult.FAIL)
            return result;
        Direction clickedFace = ctx.getClickedFace();
        if (clickedFace.getAxis() != ((AndesiteLargeBoilerBlock) getBlock()).getAxisForPlacement(ctx))
            result = super.place(BlockPlaceContext.at(ctx, ctx.getClickedPos()
                    .relative(clickedFace), clickedFace));
        if (result == InteractionResult.FAIL && ctx.getLevel()
                .isClientSide())
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> showBounds(ctx));
        return result;
    }

    @OnlyIn(Dist.CLIENT)
    public void showBounds(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Direction.Axis axis = ((AndesiteLargeBoilerBlock) getBlock()).getAxisForPlacement(context);
        Vec3 contract = Vec3.atLowerCornerOf(Direction.get(Direction.AxisDirection.POSITIVE, axis)
                .getNormal());
        if (!(context.getPlayer()instanceof LocalPlayer localPlayer))
            return;
        Outliner.getInstance().showAABB(Pair.of("waterwheel", pos), new AABB(pos).inflate(1)
                        .deflate(contract.x, contract.y, contract.z))
                .colored(0xFF_ff5d6c);
        CreateLang.translate("large_water_wheel.not_enough_space")
                .color(0xFF_ff5d6c)
                .sendStatus(localPlayer);
    }
}
