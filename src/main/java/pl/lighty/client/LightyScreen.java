package pl.lighty.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/** Menu moda - otwierane prawym Shiftem. */
public class LightyScreen extends Screen {
    private static final int PW = 220;
    private static final int PH = 150;

    public LightyScreen() {
        super(Text.literal(LightyClient.NAME));
    }

    private static Text label(String name, boolean on) {
        return Text.literal(name + ": " + (on ? "WŁ" : "WYŁ"))
                .styled(s -> s.withColor(on ? 0xD9A0FF : 0xAAAAAA));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int top = this.height / 2 - PH / 2;

        this.addDrawableChild(ButtonWidget.builder(label("Tracery", LightyClient.tracers), b -> {
            LightyClient.tracers = !LightyClient.tracers;
            b.setMessage(label("Tracery", LightyClient.tracers));
        }).dimensions(cx - 90, top + 38, 180, 20).build());

        this.addDrawableChild(ButtonWidget.builder(label("Nametagi", LightyClient.nametags), b -> {
            LightyClient.nametags = !LightyClient.nametags;
            b.setMessage(label("Nametagi", LightyClient.nametags));
        }).dimensions(cx - 90, top + 64, 180, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Gotowe"), b -> this.close())
                .dimensions(cx - 90, top + PH - 30, 180, 20).build());
    }

    @Override
    public void renderBackground(DrawContext c, int mouseX, int mouseY, float delta) {
        super.renderBackground(c, mouseX, mouseY, delta);
        int x1 = this.width / 2 - PW / 2;
        int y1 = this.height / 2 - PH / 2;
        int x2 = x1 + PW;
        int y2 = y1 + PH;
        c.fill(x1, y1, x2, y2, 0xE0140A24);
        c.fill(x1, y1, x2, y1 + 26, LightyClient.PURPLE_DARK);
        c.fill(x1, y1, x2, y1 + 1, LightyClient.PURPLE);
        c.fill(x1, y2 - 1, x2, y2, LightyClient.PURPLE);
        c.fill(x1, y1, x1 + 1, y2, LightyClient.PURPLE);
        c.fill(x2 - 1, y1, x2, y2, LightyClient.PURPLE);
    }

    @Override
    public void render(DrawContext c, int mouseX, int mouseY, float delta) {
        super.render(c, mouseX, mouseY, delta);
        int y1 = this.height / 2 - PH / 2;
        c.drawCenteredTextWithShadow(this.textRenderer, Text.literal(LightyClient.NAME), this.width / 2, y1 + 5, LightyClient.PURPLE_LIGHT);
        c.drawCenteredTextWithShadow(this.textRenderer, Text.literal("autor: " + LightyClient.AUTHOR), this.width / 2, y1 + 15, 0xFFAAAAAA);
    }
}
