#include <Adafruit_NeoPixel.h>
#define NUMPIXELS 60
Adafruit_NeoPixel strip = Adafruit_NeoPixel(NUMPIXELS, 6);
byte inputByte;
int currentPixel = 0, currentColor = 0, r = 0, g = 0, b = 0;
uint32_t color;

void setup() {
    Serial.begin(19200);
    strip.begin();
    strip.show();
}

void serialEvent() {
    while (Serial.available()) {
        inputByte = Serial.read(); 
        (currentColor == 0 ? r : currentColor == 1 ? g : b) = inputByte;
        strip.setPixelColor(currentPixel, r, g, b);
        if (++currentColor == 3) {
            currentColor = 0;
            if (++currentPixel == NUMPIXELS) {
                currentPixel = 0;
                strip.show();
            }
            color = strip.getPixelColor(currentPixel);
            r = color >> 16 & 0xFF;
            g = color >>  8 & 0xFF;
            b = color       & 0xFF;
        }
    }
}

void loop() {}
