package com.stefanolupo.ndngame.backend.subscriber;

import com.stefanolupo.ndngame.backend.ndn.FaceManager;
import com.stefanolupo.ndngame.backend.statistics.Histogram;
import com.stefanolupo.ndngame.names.SequenceNumberedName;
import net.named_data.jndn.Data;
import net.named_data.jndn.Interest;
import net.named_data.jndn.OnData;
import net.named_data.jndn.OnTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class BaseSubscriber<D> implements OnData, OnTimeout {
    private static final Logger LOG = LoggerFactory.getLogger(BaseSubscriber.class);

    /**
     * The timeout of the interest
     */
    private static final long INTEREST_LIFETIME_MS = 1000;

    private static final long MIN_SLEEP_TIME_TO_BOTHER_MS = 10;

    private SequenceNumberedName name;
    private D entity;
    private final Function<Data, D> dataFunction;
    private final Function<D, Long> sleepTimeFunction;
    private final Function<Data, SequenceNumberedName> nameExtractor;
    private final Histogram histogram;

    private final FaceManager faceManager;
    private long lastInterestExpressTime = 0;


    public BaseSubscriber(FaceManager faceManager,
                          SequenceNumberedName name,
                          Function<Data, D> dataFunction,
                          Function<Data, SequenceNumberedName> nameExtractor,
                          Function<D, Long> sleepTimeFunction,
                          Histogram histogram) {
        this.faceManager = faceManager;
        this.name = name;
        this.dataFunction = dataFunction;
        this.nameExtractor = nameExtractor;
        this.sleepTimeFunction = sleepTimeFunction;
        this.histogram = histogram;
        expressInterestSafe(buildInterest(name));
    }

    public BaseSubscriber(FaceManager faceManager,
                          SequenceNumberedName name,
                          Function<Data, D> dataFunction,
                          Function<Data, SequenceNumberedName> nameExtractor) {
        this(faceManager,
                name,
                dataFunction,
                nameExtractor,
                l -> 10L,
                null);
    }

    @Override
    public void onData(Interest interest, Data data) {
        long now = System.currentTimeMillis();
        long delta = now - lastInterestExpressTime;
        histogram.addDatapoint(delta);

        entity = dataFunction.apply(data);

        // Setup the name for the next data based on what came from publisher
        name = nameExtractor.apply(data);

        long targetSleepTime = sleepTimeFunction.apply(entity);

        long sleepTime = targetSleepTime - delta;
        if (sleepTime > MIN_SLEEP_TIME_TO_BOTHER_MS) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        expressInterestSafe(buildInterest(name));
    }

    public long getLatestVersionSeen() {
        return name.getLatestSequenceNumberSeen();
    }

    @Override
    public void onTimeout(Interest interest) {
//        LOG.info("Timeout for {}, resending interest", interest.toUri());
        expressInterestSafe(buildInterest(name));
    }

    public D getEntity() {
        return entity;
    }

    private Interest buildInterest(SequenceNumberedName name) {
        return name.buildInterest()
                .setMustBeFresh(true)
                .setInterestLifetimeMilliseconds(INTEREST_LIFETIME_MS)
                .setCanBePrefix(true);
    }

    private void expressInterestSafe(Interest i) {
        lastInterestExpressTime = System.currentTimeMillis();
        faceManager.expressInterestSafe(i, this, this);
    }
}
