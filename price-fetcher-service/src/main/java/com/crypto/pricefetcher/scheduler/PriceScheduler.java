package com.crypto.pricefetcher.scheduler;

import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.service.PriceFetchService;
import com.crypto.pricefetcher.service.publisher.KafkaPricePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Scheduler component responsible for triggering periodic price fetches.
 * It coordinates getting data from the service layer and publishing it to
 * Kafka.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PriceScheduler {

    private final PriceFetchService fetchService;
    private final KafkaPricePublisher publisher;

    /**
     * Periodically executes the price fetch and publish workflow.
     * The interval is configured via 'app.poll.interval' property.
     */
    @Scheduled(fixedRateString = "${app.poll.interval}")
    public void execute() {
        log.debug("Starting scheduled price fetch...");

        try {
            Map<String, CryptoPriceEvent> events = fetchService.fetchLatestPrices();

            if (!events.isEmpty()) {
                events.values().forEach(publisher::publish);
                log.info("Finished processing {} price events.", events.size());
            } else {
                log.info("No new price events to publish.");
            }
        } catch (Exception e) {
            log.error("Error during scheduled price fetch execution: {}", e.getMessage(), e);
        }
    }
}
