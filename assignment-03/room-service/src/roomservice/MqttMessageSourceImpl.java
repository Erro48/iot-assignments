package roomservice;

import java.util.function.Consumer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;

public class MqttMessageSourceImpl extends AbstractVerticle implements MessageSource {

    private final String topic;
    private Consumer<String> handler;

    public MqttMessageSourceImpl(final String topic) {
        this.topic = topic;
        this.handler = string -> System.out.println(string);
    }

    @Override
    public void start() {           
        final Vertx vertx = Vertx.vertx();
        final MqttClient client = MqttClient.create(vertx);

        client.connect(1883, "broker.mqtt-dashboard.com", c -> {
            
            client.publishHandler(s -> {
                System.out.println("There are new message in topic: " + s.topicName());
                System.out.println("Content(as string) of the message: " + s.payload().toString());
                System.out.println("QoS: " + s.qosLevel());
                
                this.handler.accept(s.payload().toString());
            })
            .subscribe(topic, 2);            
        });
    }

    @Override
    public void setMessageHandler(final Consumer<String> handler) {
        this.handler = handler;
    }

}
