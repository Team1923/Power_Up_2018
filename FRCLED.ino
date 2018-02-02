#include <stdio.h>
#include <string.h>
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif
#define PIN            6
#define NUMPIXELS      60
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);

int x=0;
int y=0;
int z=0;
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

/*
input() and setRGB() receive input from Serial. Format is as follows:
10 bytes in total
Byte 1 -> Pattern Profile ID
Byte 2-4 -> Red Value (must be between 0 and 255)
Byte 5-7 -> Green Value (must be between 0 and 255)
Byte 8-10 -> Blue Value (must be between 0 and 255)
*/
int input(){
  
    x = Serial.read()-'0';
    y = Serial.read()-'0';
    z = Serial.read()-'0';
    
    if(x > 255){
    x=255;
    }
    if(y > 255){
    y=255;
    }
    if(z > 255){
    z=255;
    }
    
    return (x*100)+(y*10)+z;
    
}


void setRGB(){

  while(Serial.available()  == 10){
    
    pattern=Serial.read()-'0';
    r=input();
    g=input();
    b=input();
    
  }

}
