package pl.lighty.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Tracery i nametagi rysowane na ekranie (rzutowanie 3D -> 2D).
 * Dzieki temu nie zalezy to od zmieniajacego sie renderera swiata.
 */
public final class Esp {
    private Esp() {}

    private static final double NEAR = 0.05;

    public static void render(DrawContext ctx, RenderTickCounter tick) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null || mc.options.hudHidden) return;

        TextRenderer tr = mc.textRenderer;
        ctx.drawTextWithShadow(tr, LightyClient.NAME + " by " + LightyClient.AUTHOR, 4, 4, LightyClient.PURPLE);
        ctx.drawTextWithShadow(tr, "Prawy Shift - menu", 4, 15, LightyClient.PURPLE_LIGHT);

        if (!LightyClient.tracers && !LightyClient.nametags) return;
        if (mc.currentScreen != null) return;

        float td = tick.getTickProgress(false);
        int w = ctx.getScaledWindowWidth();
        int h = ctx.getScaledWindowHeight();

        // baza kamery (pierwsza osoba)
        Vec3d cam = mc.player.getCameraPosVec(td);
        double yaw = Math.toRadians(mc.player.getYaw(td));
        double pitch = Math.toRadians(mc.player.getPitch(td));
        Vec3d fwd = new Vec3d(-Math.sin(yaw) * Math.cos(pitch), -Math.sin(pitch), Math.cos(yaw) * Math.cos(pitch));
        Vec3d right = new Vec3d(-Math.cos(yaw), 0, -Math.sin(yaw));
        Vec3d up = right.crossProduct(fwd);

        double fov = Math.toRadians(mc.options.getFov().getValue());
        double tanHalf = Math.tan(fov / 2.0);
        double aspect = (double) w / (double) h;

        double cx = w / 2.0;
        double cy = h / 2.0;

        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player || p.isSpectator()) continue;

            Vec3d base = p.getLerpedPos(td);
            double dist = base.distanceTo(cam);

            if (LightyClient.tracers) {
                double[] s = project(base.add(0, p.getHeight() / 2.0, 0), cam, fwd, right, up, tanHalf, aspect, w, h);
                double[] e = clamp(cx, cy, s[0], s[1], w, h, 6);
                line(ctx, cx, cy, e[0], e[1], LightyClient.PURPLE);
            }

            if (LightyClient.nametags) {
                double[] s = project(base.add(0, p.getHeight() + 0.35, 0), cam, fwd, right, up, tanHalf, aspect, w, h);
                if (s[2] > NEAR && s[0] > 0 && s[0] < w && s[1] > 0 && s[1] < h) {
                    int hp = Math.round(p.getHealth() + p.getAbsorptionAmount());
                    String text = p.getName().getString() + "  " + hp + " HP  " + Math.round(dist) + "m";
                    int tw = tr.getWidth(text);
                    int x = (int) s[0] - tw / 2;
                    int y = (int) s[1] - 10;
                    ctx.fill(x - 2, y - 2, x + tw + 2, y + 10, 0x90000000);
                    ctx.fill(x - 2, y + 10, x + tw + 2, y + 11, LightyClient.PURPLE);
                    ctx.drawTextWithShadow(tr, text, x, y, LightyClient.PURPLE_LIGHT);
                }
            }
        }
    }

    /** Zwraca {ekranX, ekranY, glebia}. Dla punktow za kamera odwraca kierunek. */
    private static double[] project(Vec3d pos, Vec3d cam, Vec3d fwd, Vec3d right, Vec3d up,
                                    double tanHalf, double aspect, int w, int h) {
        Vec3d d = pos.subtract(cam);
        double x = d.dotProduct(right);
        double y = d.dotProduct(up);
        double z = d.dotProduct(fwd);

        double depth = z;
        if (z < NEAR) {
            x = -x;
            y = -y;
            z = NEAR;
        }
        double ndcX = x / (z * tanHalf * aspect);
        double ndcY = y / (z * tanHalf);
        return new double[]{(ndcX * 0.5 + 0.5) * w, (0.5 - ndcY * 0.5) * h, depth};
    }

    /** Ogranicza koniec linii do ekranu (idzie po prostej od srodka). */
    private static double[] clamp(double cx, double cy, double ex, double ey, int w, int h, int margin) {
        double dx = ex - cx;
        double dy = ey - cy;
        double sx = Math.abs(dx) < 1e-6 ? Double.MAX_VALUE : (w / 2.0 - margin) / Math.abs(dx);
        double sy = Math.abs(dy) < 1e-6 ? Double.MAX_VALUE : (h / 2.0 - margin) / Math.abs(dy);
        double k = Math.min(1.0, Math.min(sx, sy));
        return new double[]{cx + dx * k, cy + dy * k};
    }

    /** Prosta linia z malych kwadratow 2x2. */
    private static void line(DrawContext ctx, double x0, double y0, double x1, double y1, int color) {
        double dx = x1 - x0;
        double dy = y1 - y0;
        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));
        if (steps <= 0) return;
        double sx = dx / steps;
        double sy = dy / steps;
        double x = x0;
        double y = y0;
        for (int i = 0; i <= steps; i++) {
            int px = (int) Math.round(x);
            int py = (int) Math.round(y);
            ctx.fill(px, py, px + 2, py + 2, color);
            x += sx;
            y += sy;
        }
    }
}
