package mc.flash.client;

import java.util.Random;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

public class MinecraftflashmodClient implements ClientModInitializer {

    // speed level starts with 1 and goes to 16 so minecraft doesnt break lol
    int speedLevel=1;
    
    // make it true if the up arrow was already down last tick
    boolean upWasDown= false;

    // it counts how long the picture is on the screen
    int imageTimer=0;

    // it counts how long i have been sprinting
    int sprintTicks=0;

    //choose a random place for the pic 0.0 TO 1.0 so doesnt go off the screen didnt wanna make it go right or left side
    float randomX=0.5f;
    float randomY=0.5f;

    //makes the random numbers
    Random dice= new Random();

    @Override
    public void onInitializeClient() {

        //names of my assets and their locations
        Identifier soundId=Identifier.fromNamespaceAndPath("minecraftflashmod", "speedsound");
        Identifier imageId=Identifier.fromNamespaceAndPath("minecraftflashmod", "texture/graphic/speedtrynot.png");
        Identifier hudId= Identifier.fromNamespaceAndPath("minecraftflashmod", "sprint_image");

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

                //Play my sound twin
                SoundEvent mySound=SoundEvent.createVariableRangeEvent(soundId);
                client.getSoundManager().play(SimpleSoundInstance.forUI(mySound, 1.0f));

                // New location for the picture and makes it appear
                randomX= dice.nextFloat();
                randomY=dice.nextFloat();
                imageTimer=100;
            }
            
            // remember the up arrow for the next tick
            upWasDown=up;

            //if shift and either WASD make it sprint its like the last one but moving is true too now
            if (shift==true && moving==true){
                client.player.setSprinting(true);

                //Add 1 to sprint time
                sprintTicks = sprintTicks + 1;

                //Show the pic
                imageTimer=60;

                //Make him faster and give him boost
                double boost= 1.0 +0.03*speedLevel;
                Vec3 oldSpeed=client.player.getDeltaMovement();
                client.player.setDeltaMovement(oldSpeed.x * boost, oldSpeed.y, oldSpeed.z * boost);
            } else {
                // if im not sprinting the pic timer counts down
                if (imageTimer > 0) {
                    imageTimer = imageTimer - 1;
                }
            }
        });

        // this part draws the pic on the screen every frame
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, hudId, (graphics, tracker) -> {

            // if the timer is 0 dont draw anything
            if (imageTimer <= 0) {
                return;
            }

            // how big is the screen
            int screenW = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int screenH = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            // the more i sprint and upgrade the bigger the pic gets
            float scale = 0.5f + sprintTicks / 400.0f + speedLevel * 0.25f;
            if (scale > 6.0f) {
                scale = 6.0f;
            }

            // my pic is 320 x 337 so i start small
            int width = (int) (64 * scale);
            int height = (int) (67 * scale);

            // if its bigger than the screen make it smaller
            if (width > screenW) {
                width = screenW;
            }
            if (height > screenH) {
                height = screenH;
            }

            // pick the spot so it never goes outside the screen
            int x = (int) (randomX * (screenW - width));
            int y = (int) (randomY * (screenH - height));

            // draw it
            graphics.blit(RenderPipelines.GUI_TEXTURED, imageId, x, y, 0, 0, width, height, 320, 337, 320, 337);
        });
    }
}