package com.esark.gasp;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.esark.framework.Game;
import com.esark.framework.Graphics;
import com.esark.framework.Input;
import com.esark.framework.Pixmap;
import com.esark.framework.Screen;

import static com.esark.framework.AndroidGame.bufferFlag;
import static com.esark.gasp.Assets.blueJoystick;
import static com.esark.gasp.Assets.whiteBackground;
import static com.esark.gasp.Assets.redJoystick;
import static com.esark.gasp.FFT.fft;

import java.util.List;

public class GameScreen extends Screen implements Input {
    Context context = null;

    int xStart = 0, xStop = 0;
    public static double[] A2DVal = new double[3500];
    double[] psd = new double[2048];

    private static final int INVALID_POINTER_ID = -1;
    // The ‘active pointer’ is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;


    //Constructor
    public GameScreen(Game game) {
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
        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        //Check to see if paused
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 100 && event.y < 50) {
                    //Back Button Code Here
                    Intent intent2 = new Intent(context.getApplicationContext(), GaspSemg.class);
                    context.startActivity(intent2);
                    return;
                }
                //else if (landscape == 1 && event.x < 100 && event.y > 230)
            }
        }

        //   if(landscape == 0) {
        g.drawPortraitPixmap(Assets.whiteBackground, 0, 0);
        xStart = 0;
        xStop = 1;
        int u = 0;
        //   for (int y = 1; y < 8; y++) {
        Complex[] cinput = new Complex[2048];        //256 works
        for (int m = 0; m < 2048; m++) {
            cinput[m] = new Complex(A2DVal[m], 0.0);
            xStart = xStop;
            xStop++;
        }
        fft(cinput);

    //    System.out.println("Results:");
        for (Complex c : cinput) {
       //     System.out.println(c);
            psd[u] = ((c.re * c.re + c.im * c.im) / -6000000) + 4500;
            if(psd[u] < 4000){
                psd[u] = 4000;
            }
      //      System.out.println("PSD:");
        //    System.out.println(psd[u]);
            u++;
        }
        xStart = 200;
        xStop = 201;
        for (int i = 2; i < 2048; i++) {
            g.drawBlackLine(xStart, (int) psd[i - 1], xStop, (int) psd[i], 0);
            xStart = xStop;
            xStop++;
        }
      //  while(bufferFlag == 0);


        xStart = 3500;
        xStop = 3499;
        for (int n = 3499; n > 5; n--) {
            g.drawBlackLine(xStart, (int) A2DVal[n], xStop, (int) (A2DVal[n - 5]), 0);
            xStart = xStop;
            xStop -= 5;
        }


     //   bufferFlag = 0;
       // SystemClock.sleep(10);

}

    @Override
    public void present ( float deltaTime){
        Graphics g = game.getGraphics();
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