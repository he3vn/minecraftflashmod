package mc.flash.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class MinecraftflashmodClient implements ClientModInitializer {

    // speed level starts with 1 and goes to 16 so minecraft doesnt break lol
    int speedLevel=1;
    
    // make it true if the up arrow was already down last tick
    boolean upWasDown= false;

    @Override
    public void onInitializeClient() {

        // to make sure it runs as much time per second as minecraft ticks
        ClientTickEvents.END_CLIENT_TICK.register(client-> {

            // no players should stop it completely
            if (client.player== null) {
                return;
            }

            // is the left key being used/pressed?(had to take ts part  online to register keybinds)
            boolean shift= false;
            if (InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT) == true) {
                shift=true;
            }

            // is WASD being pressed or nah?
            boolean moving=false;
            if (InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_W) == true) {
                moving = true;
            }
            if (InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_A) == true) {
                moving = true;
            }
            if (InputConstants.isKeyDown(client.getWindow(),GLFW.GLFW_KEY_S)==true) {
                moving=true;
            }
            if (InputConstants.isKeyDown(client.getWindow(),GLFW.GLFW_KEY_D)==true){
                moving=true;
            }

            // is up arrow pressed?
            boolean up=false;
            if (InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_UP) == true) {
                up=true;
            }

            // shift + up arrow = double the speed level (only once per press)
            if (shift==true && up==true && upWasDown==false) {
                if (speedLevel<16){
                    speedLevel=speedLevel*2;
                }
                System.out.println("speed level is now " + speedLevel);
            }
            
            // remember the up arrow for the next tick
            upWasDown=up;

            //if shift and either WASD make it sprint its like the last one but moving is true too now
            if (shift==true && moving==true){
                client.player.setSprinting(true);
            }
        });
    }
}