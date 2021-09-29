import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class finalProject extends PApplet {

/*

Final Project
"Laoco\u00f6n - Just keep breathing"
by Emily Lankiewicz

Instructions:
Follow all on screen prompts.

Background:
The idea behind this project is to comment on the attempt to
create a "blanket solution" for stress and panic. In addition it is about
the loss of a human element in the machines we make to fix things
in the digital age. I tried to paint distress as a variable rather
than an emotion or feeling that is not always true or false.
It's not something that you can just turn on and off.

In the myth of the Trojan war, Laoco\u00f6n, a Trojan priest, famously
warns, referring to the Trojan Horse,"Beware of Greeks bearing gifts."
He is not believed right away and because he persisted with his 
worried warnings Poseidon sent sea snakes to kill him. If he had
"stayed calm" so to speak perhaps the Trojans would have been spared.

*/




Minim minim = new Minim(this);
AudioPlayer theme;
AudioPlayer breathingSong;
AudioPlayer prompt;
AudioPlayer space;
AudioPlayer finalScreen;

PFont console;
PFont title;

int screen = 0;

public void setup() {
  
  console = createFont("joystix monospace.ttf", 60);
  title = createFont("Inversionz.otf", 150);
  noCursor();

  horse = loadImage("Horse.png");

  theme = minim.loadFile("151780__weaverfishable__8bit-mix.wav");
  breathingSong = minim.loadFile("154972__setuniman__nervous-and-calm-together-0n-16mi.wav");
  prompt = minim.loadFile("341231__jeremysykes__coin00.wav");
  space = minim.loadFile("350876__cabled-mess__coin-c-09.wav");
  finalScreen = minim.loadFile("335361__cabled-mess__little-happy-tune-22-10.wav");


  //For Splash Screen
  columns = w/scale;
  rows = h/scale;
  titleGrid = new float[columns][rows];
  for (int y = 0; y < rows; y++) {
    for (int x = 0; x < columns; x++) {
      titleGrid[x][y] = map(noise(x, y), 0, 1, -100, 100);
    }
  }
  //For Stars

  for (int i = 0; i < numberOfStars; i++) {
    x[i] = random(0, width);
    y[i] = random(0, height);
    r[i] = random(1, 3);
    brightness[i] = random(0, 255);
    brightnessChange[i] = 0;
  }
}






public void draw() {
  if (screen == 0) {
    splashScreen();
    theme.play();
  } else if (screen == 1) {
    ballScreen();
    theme.mute();
    breathingSong.play();
    prompt.rewind();
  } else if (screen == 2) {
    questionScreen();
    breathingSong.pause();
    breathingSong.rewind();
    prompt.play();
  } else if (screen == 3) {
    endScreen();
    finalScreen.play();
  }

  if (millis() > askCalmYet) {
    if (screen == 1) {
      screen = 2;
    }
  }
}

public void keyPressed() {
  if (key == ' ') {
    if (screen == 0) {
      startBreathing();
      space.play();
      space.rewind();
    }
  }

  if (screen == 2) {
    if (key == CODED) {
      if (keyCode == RIGHT) {
        arrowNoStatus=225;
        arrowYesStatus=0;
        starterArrow = 0;
      } else if (keyCode == LEFT) {
        arrowNoStatus=0;
        arrowYesStatus=225;
        starterArrow = 0;
      }
    }
  }

  if (screen == 2) {
    if ((arrowYesStatus>=225) || (starterArrow>=255)) 
      if (keyPressed) { 
        if (key == ' ') { 
          screen = 3;
          space.play();
          space.rewind();
        }
      }
  }

  if (screen == 2) {
    if (arrowNoStatus>=225) 
      if (keyPressed) { 
        if (key == ' ') { 
          startBreathing();
          space.play();
          space.rewind();
        }
      }
  }
}

public void startBreathing() {
  screen=1;
  askCalmYet = millis() + 30 * 1000;
}
int numberOfStars = 400;
float[] x = new float[numberOfStars];
float[] y = new float[numberOfStars];
float[] r = new float[numberOfStars];
float [] brightness = new float [numberOfStars];
float [] brightnessChange = new float [numberOfStars];

float timeToShrink = -1;
float timeToExpand = 0;

int min = 125;
int max = 300;

int growth = 125;
int sizeChange = 0;

float spin = 0;

float shift = 0;
float sphereColor = 0;

float backgroundColor = 0;
float backgroundChange = 0;

float progress = 0;

float askCalmYet = -1;

public void ballScreen(){
  background(backgroundColor, 45, 107);
  fill(backgroundColor, 45, 107);
  rect(0,0,width,height);
  backgroundColor +=backgroundChange;

  if (backgroundColor<=20) {
    backgroundChange+=.005f;
  }

  if (backgroundColor>=90) {
    backgroundChange+= -.005f;
  }

  noStroke();

  ///////////////////////// Stars //////////////////////////////

  for (int i = 0; i < numberOfStars; i++) {
    fill(random(150, 255), brightness[i]);
    ellipse(x[i], y[i], r[i], r[i]);

    /// Star brightness //
    brightness[i] +=brightnessChange[i];

    if (brightness[i]<=0) {
      brightnessChange[i]+=3;
    }

    if (brightness[i]>=255) {
      brightnessChange[i]+=-3;
    }
  }



  ///////////////////// Moving Spheres //////////////////////////////////////////////
  strokeWeight(1.5f);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 100));
  translate(width/2, height/2);
  rotateZ(spin);
  rotateX(spin);
  sphereDetail(PApplet.parseInt(8 * progress) + 3);
  sphere(growth);
  popMatrix();

  strokeWeight(1.5f);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 100));
  translate(width/2, height/2);
  rotateZ(spin+3);
  rotateX(spin+1);
  sphereDetail(PApplet.parseInt(8 * progress) + 3);
  sphere(growth);
  popMatrix();


  /////////// Smallest Sphere ///////////////////////////////
  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 75));
  translate(width/2, height/2);
  rotateZ(spin+5);
  rotateX(spin+1);
  sphereDetail(PApplet.parseInt(8 * progress) + 3);
  sphere(min);
  popMatrix();

  /////////// Largest Sphere ///////////////////////////////
  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 60));
  translate(width/2, height/2);
  rotateY(spin-5);
  rotateX(spin-10);
  sphereDetail(PApplet.parseInt(8 * progress) + 3);
  sphere(max);
  popMatrix();

  ////////////////// Rotation ///////////////////////////////
  spin += .005f;

  /// Color Change ////
  sphereColor +=shift;

  if (sphereColor<=0) {
    shift=1;
  }

  if (sphereColor>=255) {
    shift=-1;
  }


  /////////////////////////// Size & Polygon Change ////////////////////////////////
  float maxDiff = max - min;
  float curDiff = growth - min;
  progress = curDiff/maxDiff;


  if (timeToExpand >= 0) {
    if (millis() >= timeToExpand) {
      if (growth <= max) {
        growth = growth+1;
      } else {
        //done rising
        timeToShrink = millis() +  1.5f * 1000;
        timeToExpand = -1;
      }
    }
  } else if (timeToShrink >= 0) {
    if (millis() >= timeToShrink) {
      if (growth >= min) {
        growth = growth-1;
      } else {
        timeToExpand = millis() +  1.5f * 1000;
        timeToShrink = -1;
      }
    }
  }
}
PImage horse;

public void endScreen() {
  background(0xff383E7C);

  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 60));
  translate(width/2, height/2);
  rotateY(spin-2);
  rotateX(spin-1);
  sphereDetail(3);
  sphere(max);
  popMatrix();

  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 60));
  translate(width/2, height/2);
  rotateY(spin-2);
  rotateX(spin-5);
  sphereDetail(3);
  sphere(400);
  popMatrix();
  
  spin += .05f;

  for (int i = 0; i < numberOfStars; i++) {
    fill(random(150, 255), brightness[i]);
    ellipse(x[i], y[i], r[i], r[i]);
  }
  fill(titleColor, 160, 255, random(220, 250));
  textFont(console);
  textSize(30);
  textAlign(CENTER);
  text("Congratulations", width/2, 360);
  textSize(20);
  text("You are now calm", width/2, 410);
  
  titleColor +=titleShift;
    if (titleColor<=0) {
    titleShift=1;
  }

  if (titleColor>=255) {
    titleShift=-1;
  }

  fill(titleColor, 160, 255, random(220, 250));
  textFont(console);
  textSize(19);
  textAlign(CENTER);
  text("Press ESC and carry on", width/2, 540);
  fill(0xffFF83E6, random(220, 255));
  text("      ESC             ", width/2, 540);



  tint(titleColor, 160, 255, random(200, 250));
  image(horse, 480, 200, 50, 70);
}
float arrowYesStatus;
float arrowNoStatus;
float starterArrow=255;

public void questionScreen() {
  background(50, 45, 107);


  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 60));
  translate(width/2, height/2);
  rotateY(spin-5);
  rotateX(spin-10);
  sphereDetail(3);
  sphere(max);
  popMatrix();


  strokeWeight(1);
  noFill();
  pushMatrix();
  stroke(sphereColor, 149, 242, random(50, 75));
  translate(width/2, height/2);
  rotateZ(spin+5);
  rotateX(spin+1);
  sphereDetail(3);
  sphere(min);
  popMatrix();

  spin += .005f;

  fill(0xffFF83E6, random(200, 255));
  textFont(console);
  textSize(25);
  text("Yes", (width/2)-130, (height/2)+100);

  fill(0xffFF83E6, arrowYesStatus);
  textFont(console);
  textSize(25);
  text(">", (width/2)-178, (height/2)+100);

  fill(0xffFF83E6, starterArrow);
  textFont(console);
  textSize(25);
  text(">", (width/2)-178, (height/2)+100);

  fill(0xffFF83E6, random(200, 255));
  textFont(console);
  textSize(25);
  text("No", (width/2)+120, (height/2)+100);

  fill(0xffFF83E6, arrowNoStatus);
  textFont(console);
  textSize(25);
  text(">", (width/2)+80, (height/2)+100);

  fill(0xff688CF5, random(220, 255));
  textFont(console);
  textSize(27);
  textAlign(CENTER);
  text("Are you calm yet?", width/2, 250);

  fill(0xff688CF5, random(220, 255));
  textFont(console);
  textSize(17);
  textAlign(CENTER);
  text("Use the arrows to move and space to select the right answer.", width/2, 350);
  fill(0xffFF83E6, random(220, 255));
  textFont(console);
  textSize(17);
  textAlign(CENTER);
  text("        arrows             space                            ", width/2, 350);
}
int columns;
int rows;
int scale = 70;
int w = 2000;
int h = 2000;

float titleShift = 0;
float titleColor = 0;

float [][] titleGrid;

public void splashScreen() {
  background(50, 45, 107);
  //Title Page
  textFont(title);
  fill(titleColor, 160, 255, random(230, 250));
  text("laoco\u00f6n", 220, 300);
  titleColor +=titleShift;

  if (titleColor<=0) {
    titleShift=1;
  }

  if (titleColor>=255) {
    titleShift=-1;
  }

  fill(titleColor, 160, 255, random(220, 255));
  textFont(console);
  textSize(20);
  text("Just keep breathing.", 500, height/2);

  fill(0xff7779E3, random(220, 255));
  textSize(15);
  text("version 3.14", 10, 25);
  textSize(12);
  text("\u00a92017 Poseidon inc. ", 800, 690);

  fill(0xffC0B8FC,random(220, 255));
  textSize(27);
  text("Press space to begin", 280, 600);


  //Title Page background  
  stroke(titleColor, 160, 255, random(20, 40));
  noFill();

  translate(width/2, height/2);
  rotateX(PI/3);

  translate(-w/2, -h/2);

  for (int y = 0; y < rows-1; y++) {
    beginShape(TRIANGLE_STRIP);
    for (int x = 0; x < columns; x++) {
      vertex (x*scale, y*scale, titleGrid[x][y]);
      vertex (x*scale, (y+1)*scale, titleGrid[x][y+1]);
    }
    endShape();
  }
}
  public void settings() {  size(1000, 700, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#080101", "--hide-stop", "finalProject" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
