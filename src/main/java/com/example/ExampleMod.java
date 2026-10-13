package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.village.TradeOffer;

public class ExampleMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Registrar el evento para dibujar en el HUD/Pantalla
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null || client.world == null || client.player == null) return;

            // Verificar si el jugador está mirando directamente a una entidad
            Entity target = client.targetedEntity;

            if (target instanceof VillagerEntity villager) {
                // Comprobar si el aldeano tiene tradeos agotados
                boolean necesitaRestock = false;
                if (villager.getOffers() != null) {
                    for (TradeOffer offer : villager.getOffers()) {
                        if (offer.isDisabled()) {
                            necesitaRestock = true;
                            break;
                        }
                    }
                }

                TextRenderer textRenderer = client.textRenderer;
                String mensaje;
                int color;

                if (necesitaRestock) {
                    mensaje = "Aldeano: Esperando Restock / Cooldown";
                    color = 0xFFFF5555; // Texto rojo
                } else {
                    mensaje = "Aldeano: Listo para comerciar";
                    color = 0xFF55FF55; // Texto verde
                }

                // Dibujar el texto en la parte superior central de la pantalla
                int x = client.getWindow().getScaledWidth() / 2 - textRenderer.getWidth(mensaje) / 2;
                int y = 20;
                drawContext.drawTextWithShadow(textRenderer, mensaje, x, y, color);
            }
        });
    }
}
