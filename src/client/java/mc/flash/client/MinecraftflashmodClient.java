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

            // are others key pressed?
            boolean otherKey = false;
            for (int i = 32; i < 349; i++) {
                if (i != GLFW.GLFW_KEY_LEFT_SHIFT) {
                    if (InputConstants.isKeyDown(client.getWindow(), i) == true) {
                        otherKey = true;
                    }
                }
            }

            // ok this time to sprint u need sprint plus another key im not sure how to do for wasd now but i will try with this
            if (shift==true && otherKey==true){
                client.player.setSprinting(true);
            }
        });
    }
}
