package mc.flash.client;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class MinecraftflashmodClient implements ClientModInitializer {
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

            // ok same code i js changed the variable and now it uses wasd
            if (shift==true && moving==true){
                client.player.setSprinting(true);
            }
        });
    }
}
