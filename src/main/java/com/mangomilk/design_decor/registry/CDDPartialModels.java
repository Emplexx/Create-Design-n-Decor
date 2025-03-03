package com.mangomilk.design_decor.registry;

import com.mangomilk.design_decor.DesignDecor;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CDDPartialModels {

    public static final PartialModel
            EMPTY = block("empty"),

            COGWHEEL = block("industrial_gear"),
            SHAFTLESS_LARGE_COGWHEEL = block("industrial_gear_large"),

            GRANITE_CW = block("granite_crushing_wheel/block"),
            DIORITE_CW = block("diorite_crushing_wheel/block"),
            LIMESTONE_CW = block("limestone_crushing_wheel/block"),
            SCORCHIA_CW = block("scorchia_crushing_wheel/block"),
            SCORIA_CW = block("scoria_crushing_wheel/block"),
            TUFF_CW = block("tuff_crushing_wheel/block"),
            VERIDIUM_CW = block("veridium_crushing_wheel/block"),
            DRIPSTONE_CW = block("dripstone_crushing_wheel/block"),
            DEEPSLATE_CW = block("deepslate_crushing_wheel/block"),
            CRIMSITE_CW = block("crimsite_crushing_wheel/block"),
            CALCITE_CW = block("calcite_crushing_wheel/block"),
            ASURINE_CW = block("asurine_crushing_wheel/block"),
            OCHRUM_CW = block("ochrum_crushing_wheel/block"),

            GRANITE_MILLSTONE_COG = block("granite_millstone/inner"),
            DIORITE_MILLSTONE_COG = block("diorite_millstone/inner"),
            LIMESTONE_MILLSTONE_COG = block("limestone_millstone/inner"),
            SCORCHIA_MILLSTONE_COG = block("scorchia_millstone/inner"),
            SCORIA_MILLSTONE_COG = block("scoria_millstone/inner"),
            TUFF_MILLSTONE_COG = block("tuff_millstone/inner"),
            VERIDIUM_MILLSTONE_COG = block("veridium_millstone/inner"),
            DRIPSTONE_MILLSTONE_COG = block("dripstone_millstone/inner"),
            DEEPSLATE_MILLSTONE_COG = block("deepslate_millstone/inner"),
            CRIMSITE_MILLSTONE_COG = block("crimsite_millstone/inner"),
            CALCITE_MILLSTONE_COG = block("calcite_millstone/inner"),
            ASURINE_MILLSTONE_COG = block("asurine_millstone/inner"),
            OCHRUM_MILLSTONE_COG = block("ochrum_millstone/inner"),

            STEPPED_LEVER_HANDLE = block("stepped_lever/lever"),

            CEILING_FAN = block("ceiling_fan/fan")


    ;




    private static PartialModel block(String path) {
        return PartialModel.of(DesignDecor.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return PartialModel.of(DesignDecor.asResource("entity/" + path));
    }

    public static void init() {
    }
}
