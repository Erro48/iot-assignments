#include <Arduino.h>
#include "PubSubClient.h"
#include "ConnectionTask.h"

#define MSG_BUFFER_SIZE 50

char msg[MSG_BUFFER_SIZE];

ConnectionTask::ConnectionTask(bool *someoneIn, int *luminosity) {
    _luminosity = luminosity;
    _someoneIn = someoneIn;
    _oldLum = *luminosity;
    _oldSi = *someoneIn;

    _client = new PubSubClient(_espClient);

    _setupWifi();
    _client->setServer(_mqttServer, _serverPort);
}

void ConnectionTask::tick() {
    bool currentSi = *_someoneIn;
    int currentLum = *_luminosity;

    if (!_client->connected()) {
        _reconnect();
    }
    _client->loop();

    unsigned long now = millis();
    if (now - _lastMsgTime > 100) {
        _lastMsgTime = now;

        if (_oldLum != currentLum || _oldSi != currentSi) {
          snprintf(msg, MSG_BUFFER_SIZE, "{\"light\": %ld, \"pir\": %s}", currentLum, currentSi ? "true" : "false");
          _client->publish(_topic, msg);
        }
    }

    _oldSi = currentSi;
    _oldLum = currentLum;
}

void ConnectionTask::_setupWifi() {
    delay(10);
    Serial.println(String("Connecting to ") + _ssid);

    WiFi.mode(WIFI_STA);
    WiFi.begin(_ssid, _password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
}

void ConnectionTask::_reconnect() {
  
  while (!_client->connected()) {
    Serial.print("Attempting MQTT connection...");
    
    // Create a random client ID
    String clientId = String("esiot-2122-client-")+String(random(0xffff), HEX);

    // Attempt to connect
    if (_client->connect(clientId.c_str())) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      _client->subscribe(_topic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(_client->state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}