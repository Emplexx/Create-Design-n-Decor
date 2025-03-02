package com.mangomilk.design_decor.blocks.ceiling_fan;

import com.mangomilk.design_decor.registry.CDDPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

// TODO large ass todo

public class CeilingFanVisual extends KineticBlockEntityVisual<CeilingFanBlockEntity> {

    protected final RotatingInstance shaft;
    protected final RotatingInstance fan;

    public CeilingFanVisual(VisualizationContext context, CeilingFanBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        fan = instancerProvider()
                .instancer(AllInstanceTypes.ROTATING, Models.partial(CDDPartialModels.CEILING_FAN))
                .createInstance();
        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();




    }

    private void animate(float angle) {
//        PoseStack ms = new PoseStack();
//        TransformStack msr = TransformStack.of(ms);

//        msr.translate(getVisualPosition());
//        msr.center()
//
//                .rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis), AngleHelper.rad(angle))
//                .unCentre();
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(fan);
    }

    @Override
    public void update(float partialTick) {
        fan.setup(blockEntity).setChanged();
    }

    @Override
    public void updateLight(float v) {

    }

    @Override
    protected void _delete() {
        fan.delete();
    }

}
