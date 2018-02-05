#include <stdio.h>
#include <string.h>
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif
#define PIN            6
#define NUMPIXELS      60
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);


int pattern=0;
int r=0;
int g=0;
int b=0;

void setup() {
  Serial.begin(19200);   
  pixels.begin();
}

void loop() {

  //Patterns - In Progress  
  switch ( pattern ) {
    
   case 0:
  //ID 0 : LED OFF
    for(int i=0;i<NUMPIXELS;i++){
      pixels.setPixelColor(i,0,0,0); 
    } 
    pixels.show();
  break;
     
  case 1:
  //ID 1 : SOLID COLOR
    for(int i=0;i<NUMPIXELS;i++){
      pixels.setPixelColor(i,r,g,b); 
    } 
    pixels.show();
  break;
  
  case 2:
  //ID 2 : ????
  break;

  case 3:
  //ID 3 : ????
  break;

  case 4:
  //ID 4 : ????
  break;
  
  default:
  //DEFAULT : SOLID COLOR
    for(int i=0;i<NUMPIXELS;i++){
      pixels.setPixelColor(i,r,g,b); 
    } 
    pixels.show();
  break;

  
} 

}



void setRGB(){

  while(Serial.available()  == 4){
    
    pattern=Serial.read()-'0';
    r=Serial.read()-'0';
    g=Serial.read()-'0';
    b=Serial.read()-'0';
    
  }

}
