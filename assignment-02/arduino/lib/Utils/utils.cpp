#include <constants.h>

float sonarPulseToMeter(float value) {
    float t = value / 1000.0 / 1000.0 / 2;
    float distance = t * SOUND_SPEED;
    distance *= 100;
    return distance;
}

int distanceToWaterLevel(int distance) {
    return RIVERBED_LEVEL - distance;
}