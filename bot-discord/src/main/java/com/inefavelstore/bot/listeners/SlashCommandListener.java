package com.inefavelstore.bot.listeners;

import com.inefavelstore.bot.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.Color;

public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("store_new")) {
            if (!Config.STORE_NEW_ATIVO) {
                event.reply("O comando /store_new esta desativado no momento.").setEphemeral(true).queue();
                return;
            }

            Guild guild = event.getGuild();
            if (guild == null) return;

            event.deferReply(true).queue();

            guild.createCategory(". TICKETS").queue(catTicket -> {
                guild.createTextChannel("chat-temp", catTicket).queue();
            });

            guild.createCategory(". CALCULADORA").queue(catCalc -> {
                guild.createTextChannel("calculadora", catCalc).queue(ch -> Config.ID_CANAL_CALCULADORA = ch.getIdLong());
            });

            guild.createCategory(". LOGS").queue(catLogs -> {
                guild.createTextChannel("logs", catLogs).queue(ch -> Config.ID_CANAL_LOGS_VENDAS = ch.getIdLong());
            });

            event.getHook().sendMessage("Estrutura completa da loja criada com sucesso!").setEphemeral(true).queue();
        }

        if (event.getName().equals("ver_vitrine")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("Vitrine Completa de Produtos - " + Config.NOME_LOJA);
            embed.setDescription("Confira abaixo nossos produtos e modalidades de compra:");
            embed.setColor(Color.MAGENTA);
            embed.setThumbnail(Config.LOGO_LOJA_URL);

            event.replyEmbeds(embed.build())
                    .addActionRow(
                            Button.secondary("btn_vitrine_robux", "Comprar Robux"),
                            Button.secondary("btn_vitrine_gamepass", "Comprar Gamepass"),
                            Button.secondary("btn_vitrine_contas", "Comprar Contas")
                    )
                    .setEphemeral(true)
                    .queue();
        }
    }
}