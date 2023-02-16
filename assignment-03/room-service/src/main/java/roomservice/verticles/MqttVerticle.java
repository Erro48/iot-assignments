package roomservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.mqtt.MqttClient;

public class MqttVerticle extends AbstractVerticle {

    private static final int MQTT_PORT = 1883;
    private static final int QOS_LEVEL = 2;
    
    private final String topic;
    private final String address;

    public MqttVerticle(final String address, final String topic) {
        this.topic = topic;
        this.address = address;
    }

    @Override
    public void start() {           
        final MqttClient client = MqttClient.create(vertx);
        final EventBus eventBus = vertx.eventBus();
           
        client.connect(MQTT_PORT, this.address, c -> {
            client.publishHandler(s -> {
                eventBus.publish("mqtt", s.payload().toString());
            })
            .subscribe(this.topic, QOS_LEVEL);            
        });
    }

}
