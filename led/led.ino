#include <Adafruit_NeoPixel.h>
#define BAUD 19200
#define NUM_PIXELS 60
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUM_PIXELS, 6);
int currentPixel, currentColor;
byte r, g, b;

void setup() {
    Serial.begin(BAUD);
    strip.begin();
    strip.show();
}

void serialEvent() {
    while (Serial.available()) {
        byte inputByte = Serial.read(); 
        (currentColor == 0 ? r : currentColor == 1 ? g : b) = inputByte;
        strip.setPixelColor(currentPixel, r, g, b);
        if (++currentColor == 3) {
            currentColor = 0;
            if (++currentPixel == NUM_PIXELS) {
                currentPixel = 0;
                strip.show();
            }
            uint32_t color = strip.getPixelColor(currentPixel);
            r = color >> 16;
            g = color >>  8;
            b = color;
        }
    }
}

void loop() {}
