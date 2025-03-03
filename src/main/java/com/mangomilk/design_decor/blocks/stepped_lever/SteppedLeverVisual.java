package com.mangomilk.design_decor.blocks.stepped_lever;

import com.mangomilk.design_decor.registry.CDDPartialModels;
import com.simibubi.create.content.redstone.analogLever.AnalogLeverBlock;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.transform.Rotate;
import dev.engine_room.flywheel.lib.transform.Translate;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.world.level.block.state.properties.AttachFace;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SteppedLeverVisual extends AbstractBlockEntityVisual<SteppedLeverBlockEntity> implements SimpleDynamicVisual {

    protected final TransformedInstance handle;

    final float rX;
    final float rY;

    public SteppedLeverVisual(VisualizationContext context, SteppedLeverBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        handle = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(CDDPartialModels.STEPPED_LEVER_HANDLE))
                .createInstance();

        AttachFace face = blockState.getValue(AnalogLeverBlock.FACE);
        rX = face == AttachFace.FLOOR ? 0 : face == AttachFace.WALL ? 90 : 180;
        rY = AngleHelper.horizontalAngle(blockState.getValue(AnalogLeverBlock.FACING));

        animateLever(partialTick);
    }

    @Override
    public void beginFrame(Context context) {
        if (!blockEntity.clientState.settled()) animateLever(context.partialTick());
    }

    private <T extends Translate<T> & Rotate<T>> T transform(T msr) {
        return msr.translate(getVisualPosition())
                .center()
                .rotate((float) (rY / 180 * Math.PI), Direction.UP)
                .rotate((float) (rX / 180 * Math.PI), Direction.EAST)
                .uncenter();
    }

    protected void animateLever(float pt) {

        float state = blockEntity.clientState.getValue(pt);
        float angle = (float) ((state / 15) * 90 / 180 * Math.PI);

        transform(handle.setIdentityTransform()).translate(1 / 2f, 1 / 4f, 1 / 2f)
                .rotate(angle, Direction.EAST)
                .translate(-1 / 2f, -1 / 4f, -1 / 2f)
                .setChanged();
    }

    @Override
    protected void _delete() {
        handle.delete();
    }

    @Override
    public void updateLight(float v) {
        relight(handle);
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(handle);
    }
}
