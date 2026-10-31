package com.NotificationHub.config;

import com.NotificationHub.deliverylog.service.DeliveryLogService;
import com.NotificationHub.notification.entity.NotificationEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;

@Configuration
public class KafkaConfig {

        @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
        private String bootstrapServers;

        @Value("${notifyhub.kafka.topic:notification-events}")
        private String notificationTopic;

        @Bean
        public NewTopic notificationTopic() {

                return new NewTopic(
                                notificationTopic,
                                3,
                                (short) 1);
        }

        @Bean
        public NewTopic notificationDeadLetterTopic() {

                return new NewTopic(
                                notificationTopic + ".DLT",
                                3,
                                (short) 1);
        }

        @Bean
        public ConsumerFactory<String, NotificationEvent> notificationConsumerFactory() {

                var deserializer = new JsonDeserializer<>(NotificationEvent.class);

                deserializer.addTrustedPackages("com.NotificationHub");

                var properties = new HashMap<String, Object>();

                properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

                properties.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");

                properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

                properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

                properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

                return new DefaultKafkaConsumerFactory<>(
                                properties,
                                new StringDeserializer(),
                                deserializer);
        }

        @Bean
        public DeadLetterPublishingRecoverer deadLetterPublishingRecoverer(
                        KafkaTemplate<String, NotificationEvent> kafkaTemplate,
                        DeliveryLogService deliveryLogService) {

                return new NotifyHubDeadLetterRecoverer(
                                kafkaTemplate,
                                deliveryLogService);
        }

        @Bean
        public DefaultErrorHandler kafkaErrorHandler(
                        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer) {

                ExponentialBackOff backOff = new ExponentialBackOff();

                /*
                 * Initial retry delay:
                 * 1 second
                 */
                backOff.setInitialInterval(
                                1000L);

                /*
                 * Delay multiplier:
                 * 2x
                 */
                backOff.setMultiplier(
                                2.0);

                /*
                 * Maximum delay between retries:
                 * 10 seconds
                 */
                backOff.setMaxInterval(
                                10000L);

                /*
                 * Maximum retry attempts:
                 * 4 retries
                 *
                 * Approx:
                 *
                 * 1 sec
                 * 2 sec
                 * 4 sec
                 * 8 sec
                 */
                backOff.setMaxElapsedTime(
                                15000L);

                DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                                deadLetterPublishingRecoverer,
                                backOff);

                return errorHandler;
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> notificationKafkaListenerContainerFactory(
                        ConsumerFactory<String, NotificationEvent> consumerFactory,
                        DefaultErrorHandler errorHandler) {

                ConcurrentKafkaListenerContainerFactory<String, NotificationEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

                factory.setConsumerFactory(
                                consumerFactory);

                factory.setCommonErrorHandler(
                                errorHandler);

                return factory;
        }
}