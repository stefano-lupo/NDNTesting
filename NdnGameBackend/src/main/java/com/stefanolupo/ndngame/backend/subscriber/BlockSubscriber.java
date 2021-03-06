package com.stefanolupo.ndngame.backend.subscriber;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hubspot.liveconfig.value.Value;
import com.stefanolupo.ndngame.backend.chronosynced.OnPlayersDiscovered;
import com.stefanolupo.ndngame.backend.ndn.FaceManager;
import com.stefanolupo.ndngame.backend.subscriber.metrics.BaseSubscriberMetricsFactory;
import com.stefanolupo.ndngame.backend.subscriber.metrics.BaseSubscriberMetricsNames;
import com.stefanolupo.ndngame.config.LocalConfig;
import com.stefanolupo.ndngame.names.blocks.BlockName;
import com.stefanolupo.ndngame.names.blocks.BlocksSyncName;
import com.stefanolupo.ndngame.protos.Block;
import com.stefanolupo.ndngame.protos.Blocks;
import com.stefanolupo.ndngame.protos.Player;
import net.named_data.jndn.Data;
import net.named_data.jndn.Interest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Singleton
public class BlockSubscriber implements OnPlayersDiscovered {

    private static final Logger LOG = LoggerFactory.getLogger(BlockSubscriber.class);

    private final Set<BaseSubscriber<Map<BlockName, Block>>> subscribersList = ConcurrentHashMap.newKeySet();
    private final LocalConfig localConfig;
    private final FaceManager faceManager;
    private final BaseSubscriberMetricsFactory metricsFactory;
    private final Value<Long> waitTime;

    @Inject
    public BlockSubscriber(LocalConfig localConfig,
                           FaceManager faceManager,
                           BaseSubscriberMetricsFactory metricsFactory,
                           @Named("block.sub.inter.interest.max.wait.time.ms") Value<Long> maxWaitTime) {
        this.localConfig = localConfig;
        this.faceManager = faceManager;
        this.metricsFactory = metricsFactory;
        this.waitTime = maxWaitTime;
    }

    public void addSubscription(BlocksSyncName blockSyncName) {
        LOG.info("Adding subscription for {}", blockSyncName.getFullName().toUri());
        BaseSubscriber<Map<BlockName, Block>> subscriber = new BaseSubscriber<>(
                faceManager,
                blockSyncName,
                this::typeFromData,
                BlocksSyncName::new,
                l -> waitTime.get(),
                metricsFactory.forNameAndType(blockSyncName.getPlayerName(), BaseSubscriberMetricsNames.ObjectType.BLOCKS));
        subscribersList.add(subscriber);
    }

    public ConcurrentMap<BlockName, Block> getRemoteBlocks() {
        ConcurrentMap<BlockName, Block> map = new ConcurrentHashMap<>();

        for (BaseSubscriber<Map<BlockName, Block>> subscriber : subscribersList) {
            // Can be null before first remote receipt of entity
            if (subscriber.getEntity() == null) {
                continue;
            }

            map.putAll(subscriber.getEntity());
        }

        return map;
    }

    public void interactWithBlock(BlockName blockName) {
        for (BaseSubscriber<Map<BlockName, Block>> subscriber : subscribersList) {
            if (subscriber.getEntity().containsKey(blockName)) {
                Interest interest = blockName.buildInterest();
                LOG.info("Interacting with block: {}", interest.toUri());
                faceManager.expressInterestSafe(interest);
                return;
            }
        }
    }

    private Map<BlockName, Block> typeFromData(Data data) {
        try {
            List<Block> blocks = Blocks.parseFrom(data.getContent().getImmutableArray()).getBlocksList();
            BlocksSyncName blocksSyncName = new BlocksSyncName(data);
            return Maps.uniqueIndex(blocks, b -> BlockName.fromBlockSyncNameAndId(blocksSyncName, b.getId()));
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("Unable to parse Block for %s" + data.getName().toUri(), e);
        }
    }

    @Override
    public void onPlayersDiscovered(Set<Player> players) {
        players.forEach(p -> this.addSubscription(new BlocksSyncName(localConfig.getGameId(), p.getName())));
    }
}
