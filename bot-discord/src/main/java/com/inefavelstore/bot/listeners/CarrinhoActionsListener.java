package com.inefavelstore.bot.listeners;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.concurrent.TimeUnit;

public class CarrinhoActionsListener extends ListenerAdapter {

    public static ActionRow[] getBotoesCarrinho() {
        return new ActionRow[]{
            ActionRow.of(
                Button.of(ButtonStyle.PRIMARY, "btn_pix", " Pegar Chave PIX"),
                Button.of(ButtonStyle.SUCCESS, "btn_qrcode", " Gerar QR Code")
            ),
            ActionRow.of(
                Button.of(ButtonStyle.SECONDARY, "btn_mudar_qtd", " Mudar Quantidade"),
                Button.of(ButtonStyle.DANGER, "btn_mudar_nick", " Não é você?")
            ),
            ActionRow.of(
                Button.of(ButtonStyle.DANGER, "btn_fechar_carrinho", " Fechar Carrinho")
            )
        };
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String componentId = event.getComponentId();

        if (componentId.equals("btn_pix")) {
            event.reply(" **Chave PIX:** `exemplo-chave-pix`").setEphemeral(true).queue();
        } else if (componentId.equals("btn_fechar_carrinho")) {
            event.reply(" Fechando carrinho em 5 segundos...").queue();
            TextChannel channel = event.getChannel().asTextChannel();
            channel.delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }
}