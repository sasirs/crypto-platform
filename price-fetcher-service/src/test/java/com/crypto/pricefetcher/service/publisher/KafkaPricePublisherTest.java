package com.crypto.pricefetcher.service.publisher;

import com.crypto.pricefetcher.model.CryptoPriceEvent;
import com.crypto.pricefetcher.model.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

class KafkaPricePublisherTest {

    @Mock
    private KafkaTemplate<String, CryptoPriceEvent> kafkaTemplate;

    private KafkaPricePublisher publisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        publisher = new KafkaPricePublisher(kafkaTemplate);
        ReflectionTestUtils.setField(publisher, "topic", "test-topic");
    }

    @Test
    void testPublish() {
        // Given
        CryptoPriceEvent event = CryptoPriceEvent.builder()
                .symbol("bitcoin")
                .price(50000.0)
                .source(ProviderType.COINGECKO)
                .build();

        // When
        publisher.publish(event);

        // Then
        verify(kafkaTemplate).send(eq("test-topic"), eq("bitcoin"), eq(event));
    }
}
