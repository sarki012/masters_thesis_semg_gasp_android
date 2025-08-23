package com.esark.gasp;

import static com.esark.gasp.GameScreen.lastEventArray;
import static com.esark.gasp.GameScreen.lastEventPSDArray;
import static com.esark.gasp.GameScreen.len;
import static java.lang.Math.sin;

import android.content.Context;
import android.content.Intent;

import com.esark.framework.Game;
import com.esark.framework.Graphics;
import com.esark.framework.Input;
import com.esark.framework.Screen;

import java.util.List;

public class GameScreenLastEvent extends Screen implements Input {
    Context context = null;

    int xStart = 0, xStop = 0;
    //public static double[] A2DVal = new double[3500];
    public static double[] A2DVal = new double[3500];
    double[] psd = new double[2048];

    double[] sineWave = new double[2048];

    double[] psdResult = new double[2048];

    private static final int INVALID_POINTER_ID = -1;
    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;


    //Constructor
    public GameScreenLastEvent(Game game) {
        super(game);
    }
    @Override
    public void update(float deltaTime, Context context) {
        //framework.input
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        updateRunning(touchEvents, deltaTime, context);
    }

    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime, Context context) {
        //updateRunning() contains controller code of our MVC scheme
        Graphics g2 = game.getGraphics();
        Assets.lastEventBackground = g2.newPixmap("lastEventBackground.png", Graphics.PixmapFormat.ARGB4444);
        len = touchEvents.size();
        //Check to see if paused
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
            }
            if (event.type == TouchEvent.TOUCH_DRAGGED || event.type == TouchEvent.TOUCH_DOWN) 
                if (event.x > 185 && event.x < 1735 && event.y > 4375 && event.y < 4650) {
                    //Artifact/PSD Screen
              //      game.setScreen(gameScreen);
                }
            }
        }
        g2.drawPortraitPixmap(Assets.lastEventBackground, 0, 0);
        xStart = 3370;
        xStop = 3369;
        for (int n = 2047; n > 5; n -= 2) {
            g2.drawBlackLine(xStart, (int) lastEventArray[n], xStop, (int) (lastEventArray[n - 2]), 0);
            xStart = xStop;
            xStop-= 5;
            if(xStart <= 380){
                break;
            }
        }

        xStart = 365;
        xStop = 366;
        for (int i = 1; i < psdResult.length; i++) {
            g2.drawRedLine(xStart, (int) lastEventPSDArray[i - 1], xStop, (int) lastEventPSDArray[i], 0);
            xStart = xStop;
            xStop += 3;
            if(xStop >= 3370){
                break;
            }
        }
    }

    @Override
    public void present ( float deltaTime){
        Graphics g2 = game.getGraphics();
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return false;
    }

    @Override
    public int getTouchX(int pointer) {
        return 0;
    }

    @Override
    public int getTouchY(int pointer) {
        return 0;
    }

    @Override
    public float getAccelX() {
        return 0;
    }

    @Override
    public float getAccelY() {
        return 0;
    }

    @Override
    public float getAccelZ() {
        return 0;
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return null;
    }
}
                    /*
                    numAvg = 10;
                    xR = xTouchRight - 3400;
                    yR = 1950 - yTouchRight;
                    if (((int)Math.sqrt(Math.abs((xR*xR + yR*yR)))) > 570) {
                        //Inverse tangent to find the angle
                        angleR = Math.atan2((double) yR, (double) xR);
                        //cos for x
                        scaledXR = (int) (570 * Math.cos(angleR));
                        //sin for y
                        scaledYR = (int) (570 * Math.sin(angleR));
                        //Save the previous values in case the user lifts a thumb
                        xPrevRight = 3400 + scaledXR;
                        yPrevRight = 1950 - scaledYR;
                        //Draw the joystick maxed out
                        g.drawJoystick(redJoystick, (3400 + scaledXR), (1950 - scaledYR));
                        //Do a numAvg moving average of the x and y coordinates of the thumb presses
                        //Shift all of the values in the temp arrays one value to the left
                        for (h = 1; h < numAvg; h++) {
                            tempCArr[h - 1] = tempCArr[h];
                            tempBArr[h - 1] = tempBArr[h];
                        }
                        //Pop the new x and y coordinates onto the stacks
                        tempCArr[numAvg - 1] = scaledXR;
                        tempBArr[numAvg - 1] = scaledYR;
                        //Once there are numAvg values in the stack rightCount = numAvg
                        if (rightCount < numAvg) {
                            rightCount++;
                        }
                        if (rightCount == numAvg) {
                            //Loop to total up the numAvg values in each array
                            for (j = 0; j < numAvg; j++) {
                                tempC += tempCArr[j];
                                tempB += tempBArr[j];
                            }
                            //The value to be sent out over Bluetooth is c. Take the average
                            c = (int) (tempC / numAvg);
                            //Make a dead zone along the y-axis. Otherwise both motors would always be spinning at the same time
                            if (c > -40 && c < 40) {
                                stopSendingCurl = 1;
                            } else {
                                stopSendingCurl = 0;
                            }
                            b = (int) (tempB / numAvg);
                            //Make a dead zone along the x-axis. Otherwise both motors would always be spinning at the same time
                            if (b > -40 && b < 40) {
                                stopSendingBoom = 1;
                            } else {
                                stopSendingBoom = 0;
                            }

                            tempC = 0;
                            tempB = 0;
                        }
                    } else if((((int)Math.sqrt(Math.abs((xR*xR + yR*yR))) <= 570))) {
                        //The thumb is within the circle. Draw the joystick at the thumb press
                        g.drawJoystick(redJoystick, xTouchRight, yTouchRight);

                        //Do a numAvg moving average of the x and y coordinates of the thumb presses
                        //Shift all of the values in the temp arrays one value to the left

                        for (k = 1; k < numAvg; k++) {
                            tempCArr[k - 1] = tempCArr[k];
                            tempBArr[k - 1] = tempBArr[k];
                        }




                        //Pop the new x and y coordinates onto the stacks
                        tempCArr[numAvg - 1] = xR;
                        tempBArr[numAvg - 1] = yR;
                        //Once there are numAvg values in the stack rightCount = numAvg
                        if (rightCount < numAvg) {
                            rightCount++;
                        }
                        if (rightCount == numAvg) {
                            //Loop to total up the numAvg values in each array
                            for (m = 0; m < numAvg; m++) {
                                tempC += tempCArr[m];
                                tempB += tempBArr[m];
                            }
                            //The value to be sent out over Bluetooth is c. Take the average
                            c = (int) (tempC / numAvg);
                            //Make a dead zone along the y-axis. Otherwise both motors would always be spinning at the same time
                            if (c > -40 && c < 40) {
                                stopSendingCurl = 1;
                            } else {
                                stopSendingCurl = 0;
                            }
                            b = (int) (tempB / numAvg);
                            //Make a dead zone along the x-axis. Otherwise both motors would always be spinning at the same time
                            if (b > -40 && b < 40) {
                                stopSendingBoom = 1;
                            } else {
                                stopSendingBoom = 0;
                            }

                            tempC = 0;
                            tempB = 0;
                        }

                    }
                    xL = xTouchLeft - 150;
                    yL = 275 - yTouchLeft;
                    if (((int)Math.sqrt(Math.abs((xL*xL + yL*yL)))) > 85) {
                        //Inverse tangent to find the angle
                        angleL = Math.atan2((double) yL, (double) xL);
                        //cos for x
                        scaledXL = (int) (85 * Math.cos(angleL));
                        //sin for y
                        scaledYL = (int) (85 * Math.sin(angleL));
                        //Save the previous values in case the user lifts a thumb
                        xPrevLeft = 140 + scaledXL;
                        yPrevLeft = 275 - scaledYL;
                        //Draw the joystick maxed out
                        g.drawCircle((140 + scaledXL), (275 - scaledYL), 45);
                        g.drawLine(140, 275, (140 + scaledXL), (275 - scaledYL), 0);
                        //Do a numAvg moving average of the x and y coordinates of the thumb presses
                        //Shift all of the values in the temp arrays one value to the left
                        for (k = 1; k < numAvg; k++) {
                            tempOArr[k - 1] = tempOArr[k];          //O for orbit
                            tempSArr[k - 1] = tempSArr[k];          //S for stick
                        }
                        //Pop the new x and y coordinates onto the stacks
                        tempOArr[numAvg - 1] = scaledXL;
                        tempSArr[numAvg - 1] = scaledYL;
                        //Once there are numAvg values in the stack rightCount = numAvg
                        if (leftCount < numAvg) {
                            leftCount++;
                        }
                        if (leftCount == numAvg) {
                            //Loop to total up the numAvg values in each array
                            for (m = 0; m < numAvg; m++) {
                                tempO += tempOArr[m];
                                tempS += tempSArr[m];
                            }
                            //o for orbit. Take the average
                            o = (int) (tempO / numAvg);
                            //Make a dead zone along the y-axis. Otherwise both motors would always be spinning at the same time
                            if (o > -40 && o < 40) {
                                stopSendingOrbit = 1;
                            } else {
                                stopSendingOrbit = 0;
                            }
                            //s for stick. Take the average
                            s = (int) (tempS / numAvg);
                            //Make a dead zone along the x-axis. Otherwise both motors would always be spinning at the same time
                            if (s > -40 && s < 40) {
                                stopSendingStick = 1;
                            } else {
                                stopSendingStick = 0;
                            }

                            tempO = 0;
                            tempS = 0;
                        }
                    } else if((((int)Math.sqrt(Math.abs((xL*xL + yL*yL))) <= 85))) {
                        //The thumb is within the circle. Draw the joystick at the thumb press
                        g.drawCircle((140 + xL), (275 - yL), 45);
                        g.drawLine(140, 275, (140 + xL), (275 - yL), 0);


                        //Do a numAvg moving average of the x and y coordinates of the thumb presses
                        //Shift all of the values in the temp arrays one value to the left
                        for (h = 1; h < numAvg; h++) {
                            tempOArr[h - 1] = tempOArr[h];
                            tempSArr[h - 1] = tempSArr[h];
                        }

                        //Pop the new x and y coordinates onto the stacks
                        tempOArr[numAvg - 1] = xL;
                        tempSArr[numAvg - 1] = yL;
                        //Once there are numAvg values in the stack rightCount = numAvg
                        if (leftCount < numAvg) {
                            leftCount++;
                        }


                        if (leftCount == numAvg) {
                            //Loop to total up the numAvg values in each array
                            for (j = 0; j < numAvg; j++) {
                                tempO += tempOArr[j];
                                tempS += tempSArr[j];
                            }
                            //o for orbit. Take the average
                            o = (int) (tempO / numAvg);
                            //Make a dead zone along the y-axis. Otherwise both motors would always be spinning at the same time
                            if (o > -40 && o < 40) {
                                stopSendingOrbit = 1;
                            } else {
                                stopSendingOrbit = 0;
                            }
                            //s for stick
                            s = (int) (tempS / numAvg);
                            //Make a dead zone along the x-axis. Otherwise both motors would always be spinning at the same time
                            if (s > -40 && s < 40) {
                                stopSendingStick = 1;
                            } else {
                                stopSendingStick = 0;
                            }

                            tempO = 0;
                            tempS = 0;
                        }


                    }
                    g.drawCircle(290, yTrackLeft, 45);
                    g.drawCircle(425, yTrackRight, 45);
                    l = 110 - yTrackLeft;
                    r = 110 - yTrackRight;
                }
              */