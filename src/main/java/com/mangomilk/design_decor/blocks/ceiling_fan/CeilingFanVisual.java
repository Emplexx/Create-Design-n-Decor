package com.mangomilk.design_decor.blocks.ceiling_fan;

import com.mangomilk.design_decor.registry.CDDPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.createmod.catnip.math.AngleHelper;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.function.Consumer;

public class CeilingFanVisual extends KineticBlockEntityVisual<CeilingFanBlockEntity> implements SimpleDynamicVisual {

    protected final RotatingInstance shaft;
    protected final TransformedInstance fan;
    protected float lastAngle = Float.NaN;

    protected final Matrix4f baseTransform = new Matrix4f();

    public CeilingFanVisual(VisualizationContext context, CeilingFanBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);

        var axis = rotationAxis();
        shaft = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(AllPartialModels.SHAFT_HALF))
                .createInstance();
        shaft.setup(blockEntity)
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, Direction.DOWN.getOpposite())
                .setChanged();

        fan = instancerProvider().instancer(AllInstanceTypes.SCROLLING_TRANSFORMED, Models.partial(CDDPartialModels.CEILING_FAN))
                .createInstance();

        Direction align = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        fan.translate(getVisualPosition())
                .center()
                .rotate(new Quaternionf().rotateTo(0, 1, 0, align.getStepX(), align.getStepY(), align.getStepZ()));

        baseTransform.set(fan.pose);

        animate(blockEntity.angle);
    }

    @Override
    public void beginFrame(Context context) {

        float partialTicks = context.partialTick();

        float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = blockEntity.angle + speed * partialTicks;

        if (Math.abs(angle - lastAngle) < 0.001)
            return;

        animate(angle);

        lastAngle = angle;
    }

    private void animate(float angle) {
        fan.setTransform(baseTransform)
                .rotateY(AngleHelper.rad(angle))
                .uncenter()
                .setChanged();
    }

    @Override
    public void update(float pt) {
        shaft.setup(blockEntity)
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        relight(shaft, fan);
    }

    @Override
    protected void _delete() {
        shaft.delete();
        fan.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(shaft);
        consumer.accept(fan);
    }

}
