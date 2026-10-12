package pl.lighty.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class LightyClient implements ClientModInitializer {
    public static final String NAME = "Lighty Client";
    public static final String AUTHOR = "L1ghtyyy";

    // Fioletowy motyw (ARGB)
    public static final int PURPLE = 0xFFB44CFF;
    public static final int PURPLE_LIGHT = 0xFFD9A0FF;
    public static final int PURPLE_DARK = 0xFF3A1670;

    // Klawisze (zmien tu, jesli chcesz inne)
    public static final int KEY_GUI = GLFW.GLFW_KEY_RIGHT_SHIFT;
    public static final int KEY_TRACERS = GLFW.GLFW_KEY_R;
    public static final int KEY_NAMETAGS = GLFW.GLFW_KEY_N;

    public static boolean tracers = true;
    public static boolean nametags = true;

    private static boolean prevGui, prevTracers, prevNametags;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            boolean gui = down(mc, KEY_GUI);
            boolean tr = down(mc, KEY_TRACERS);
            boolean nm = down(mc, KEY_NAMETAGS);

            if (mc.player != null && mc.currentScreen == null) {
                if (gui && !prevGui) mc.setScreen(new LightyScreen());
                if (tr && !prevTracers) tracers = !tracers;
                if (nm && !prevNametags) nametags = !nametags;
            }
            prevGui = gui;
            prevTracers = tr;
            prevNametags = nm;
        });

        HudElementRegistry.attachElementAfter(
                VanillaHudElements.MISC_OVERLAYS,
                Identifier.of("lighty_client", "esp"),
                Esp::render);
    }

    private static boolean down(MinecraftClient mc, int key) {
        return GLFW.glfwGetKey(mc.getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }
}
