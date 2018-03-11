package moe.clienthax.pixelmonbridge.impl.data.processor.multi.entity.player;

import com.google.common.collect.Maps;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import moe.clienthax.pixelmonbridge.api.data.key.PixelmonDataKeys;
import moe.clienthax.pixelmonbridge.api.data.manipulator.immutable.entity.player.ImmutablePartyPokemonData;
import moe.clienthax.pixelmonbridge.api.data.manipulator.mutable.entity.player.MutablePartyPokemonData;
import moe.clienthax.pixelmonbridge.impl.data.manipulator.mutable.entity.player.PixelmonMutablePartyPokemonData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.common.data.processor.common.AbstractSingleDataSingleTargetProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeMapValue;
import org.spongepowered.common.data.value.mutable.SpongeMapValue;
import org.spongepowered.common.entity.SpongeEntitySnapshot;

import java.util.Map;
import java.util.Optional;

/**
 * Created by clienthax on 10/03/2018.
 */
public class PartyPokemonProcessor extends AbstractSingleDataSingleTargetProcessor<EntityPlayer, Map<Integer, EntitySnapshot>, MapValue<Integer, EntitySnapshot>, MutablePartyPokemonData, ImmutablePartyPokemonData> {

    public PartyPokemonProcessor() {
        super(PixelmonDataKeys.PARTY_POKEMON, EntityPlayer.class);
    }

    @Override
    protected boolean set(EntityPlayer dataHolder, Map<Integer, EntitySnapshot> value) {
            PlayerStorage playerPartyStorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) dataHolder).get();
            playerPartyStorage.recallAllPokemon();
            playerPartyStorage.setPokemon(new NBTTagCompound[6]);

            NBTTagCompound[] pixelmonNBT = new NBTTagCompound[6];
            value.entrySet().stream().filter(entitySnapshotEntry -> entitySnapshotEntry.getValue() != null && entitySnapshotEntry.getValue().getType().getEntityClass().isAssignableFrom(EntityPixelmon.class)).forEach(entitySnapshotEntry -> {
                pixelmonNBT[entitySnapshotEntry.getKey()] = ((SpongeEntitySnapshot) entitySnapshotEntry.getValue()).getCompound().get();
            });
            playerPartyStorage.setPokemon(pixelmonNBT);
            return true;
    }

    @Override
    protected Optional<Map<Integer, EntitySnapshot>> getVal(EntityPlayer dataHolder) {
        Map<Integer, EntitySnapshot> entitySnapshots = Maps.newHashMap();
            PlayerStorage playerPartyStorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) dataHolder).get();
            NBTTagCompound[] partyNBTs = playerPartyStorage.getList();
            for(int i = 0; i < partyNBTs.length; i++) {
                if (partyNBTs[i] != null) {
                    EntityPixelmon pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(partyNBTs[i], dataHolder.world);
                    pixelmon.setPosition(dataHolder.posX, dataHolder.posY, dataHolder.posZ);//TODO Workaround for entitysnapshot being a dick
                    EntitySnapshot snapshot = ((Living) pixelmon).createSnapshot();
                    entitySnapshots.put(i, snapshot);
                }
            }
            return Optional.of(entitySnapshots);
    }

    @Override
    protected ImmutableValue<Map<Integer, EntitySnapshot>> constructImmutableValue(Map<Integer, EntitySnapshot> value) {
        return new ImmutableSpongeMapValue<>(getKey(), value);
    }

    @Override
    protected MapValue<Integer, EntitySnapshot> constructValue(Map<Integer, EntitySnapshot> actualValue) {
        return new SpongeMapValue<>(getKey(), actualValue);
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        return DataTransactionResult.failNoData();
    }

    @Override
    protected MutablePartyPokemonData createManipulator() {
        return new PixelmonMutablePartyPokemonData();
    }

}