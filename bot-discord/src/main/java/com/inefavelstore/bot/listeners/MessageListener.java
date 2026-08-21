package com.inefavelstore.bot.listeners;

import com.inefavelstore.bot.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.Color;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        long channelId = event.getChannel().getIdLong();

        if (Config.ID_CANAL_CALCULADORA != 0 && channelId == Config.ID_CANAL_CALCULADORA) {
            String content = event.getMessage().getContentRaw().trim();

            if (content.matches("\\d+")) {
                int qtd = Integer.parseInt(content);
                double total = qtd * Config.PRECO_ROBUX_GAMEPASS;

                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Calculadora de Cotacao - " + Config.NOME_LOJA);
                embed.setDescription(String.format(
                        "Solicitado por: %s\n\n" +
                        "Quantidade de Robux: %d\n" +
                        "Taxa por Robux: R$ %.3f\n" +
                        "Valor Total Exato: R$ %.2f",
                        event.getAuthor().getAsMention(), qtd, Config.PRECO_ROBUX_GAMEPASS, total
                ));
                embed.setColor(Color.MAGENTA);
                embed.setThumbnail(Config.LOGO_LOJA_URL);

                event.getChannel().sendMessageEmbeds(embed.build()).queue();
            }

            event.getMessage().delete().queue();
        }
    }
}