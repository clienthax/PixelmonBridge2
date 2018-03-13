package moe.clienthax.pixelmonbridge.api.data.manipulator.immutable.entity.pixelmon;

import moe.clienthax.pixelmonbridge.api.data.manipulator.mutable.entity.pixelmon.MutableEggData;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

/**
 * Created by clienthax on 12/03/2018.
 */
public interface ImmutableEggData extends ImmutableDataManipulator<ImmutableEggData, MutableEggData> {

    ImmutableValue<Boolean> isEgg();

}
