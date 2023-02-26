#ifndef __CONNECTIONTASK__
#define __CONNECTIONTASK__

#include <WiFi.h>
#include <PubSubClient.h>
#include "Task.h"

class ConnectionTask: public Task {
public:
    ConnectionTask(bool *someoneIn, int *luminosity);
    void tick() override;

private:
    const char* _ssid = "<SSID>";
    const char* _password = "<PASSWORD>";
    const char* _mqttServer = "broker.mqtt-dashboard.com";
    const int _serverPort = 1883;
    const char* _topic = "roomservice/test";

    WiFiClient _espClient;
    PubSubClient* _client;

    int *_luminosity;
    bool *_someoneIn;
    int _oldLum;
    bool _oldSi;
    int _lastMsgTime;

    void _setupWifi();
    void _reconnect();

};

#endif