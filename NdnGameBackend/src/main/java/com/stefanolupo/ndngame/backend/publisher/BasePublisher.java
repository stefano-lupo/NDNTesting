package com.stefanolupo.ndngame.backend.publisher;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.stefanolupo.ndngame.backend.ndn.FaceManager;
import com.stefanolupo.ndngame.names.SequenceNumberedName;
import net.named_data.jndn.*;
import net.named_data.jndn.util.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class BasePublisher implements OnInterestCallback {

    private static final Logger LOG = LoggerFactory.getLogger(BasePublisher.class);
    private static final Long DEFAULT_QUEUE_PROCESS_TIME_MS = 10L;
    private static final Long DEFAULT_QUEUE_PROCESS_INITIAL_WAIT_MS = 1000L;
    private static final Double FRESHNESS_PERIOD_MS = 20.0;

    private final Map<SequenceNumberedName, Face> outstandingInterests = new HashMap<>();
    private long totalNumInterests = 0;

    private final Function<Interest, SequenceNumberedName> interestTFunction;

    private Blob latestBlob;
    private AtomicBoolean hasUpdate = new AtomicBoolean(false);
    private long sequenceNumber = 0;


    @Inject
    public BasePublisher(FaceManager faceManager,
                         @Assisted Name listenName,
                         @Assisted Function<Interest, SequenceNumberedName> interestTFunction) {
        this.interestTFunction = interestTFunction;
        faceManager.registerBasicPrefix(listenName, this);
        LOG.debug("Registering {}", listenName);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                this::processQueue,
                DEFAULT_QUEUE_PROCESS_INITIAL_WAIT_MS,
                DEFAULT_QUEUE_PROCESS_TIME_MS,
                TimeUnit.MILLISECONDS);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                () -> LOG.info("Seen {} interests, {} outstanding", totalNumInterests, outstandingInterests.size()),
                10,
                60,
                TimeUnit.SECONDS
        );
    }

    /**
     * Update the blob that will be used to service interests
     * @param latestBlob the new blob to serve
     */
    public void updateLatestBlob(Blob latestBlob) {
        this.latestBlob = latestBlob;
        hasUpdate.set(true);
    }

    @Override
    public void onInterest(Name prefix, Interest interest, Face face, long interestFilterId, InterestFilter filter) {
        SequenceNumberedName interestName = interestTFunction.apply(interest);
        totalNumInterests++;
        outstandingInterests.put(interestName, face);
    }

    // TODO: Maybe multithread this?
    // TODO: Only send updates when there is one
    // TODO: Not sure about concurrent modifications here but think its okay
    private void processQueue() {
        // If had an update, consume the update


        if (hasUpdate.compareAndSet(true, false)) {
            sequenceNumber++;
//            LOG.debug("new sequence number: {}", sequenceNumber);
        } else {
            return;
        }


        // Send any interests with sequenceNumber <= currentSequenceNumber
        for (Iterator<Map.Entry<SequenceNumberedName, Face>> i = outstandingInterests.entrySet().iterator(); i.hasNext();) {
            Map.Entry<SequenceNumberedName, Face> entry = i.next();
            SequenceNumberedName sequenceNumberedName = entry.getKey();
            Face face = entry.getValue();

            if (sequenceNumberedName.getLatestSequenceNumberSeen() <= sequenceNumber) {
                sendData(sequenceNumberedName, face);
                i.remove();
            } else {
                LOG.debug("Had Update but {} already had sn {}", sequenceNumberedName.getFullName(), sequenceNumber);
            }
        }
    }

    // TODO: Naming of return here might be important
    // If i send interest for /data/45 and the sn is actually at 65 now
        // Publisher can send back something like /data/45/65
        // If another subscriber later requests /data/45, will caches send back /data/45/65?
        // Do we even want them to? The SN might be >> than 65 now..
        // Can use freshness to control this somewhat
    // If we're always sending back newest one --> we don't need sequence numbers

    private void sendData(SequenceNumberedName name, Face face) {
        name.setNextSequenceNumber(sequenceNumber);
        Data data = new Data(name.getFullName()).setContent(latestBlob);
        data.getMetaInfo().setFreshnessPeriod(FRESHNESS_PERIOD_MS);
        try {
//            keyChain.sign(data, certificateName);
            face.putData(data);
        } catch (Exception e) {
            LOG.error("Unable to send data to satisfy interest " + name.getFullName(), e);
        }
    }

}
