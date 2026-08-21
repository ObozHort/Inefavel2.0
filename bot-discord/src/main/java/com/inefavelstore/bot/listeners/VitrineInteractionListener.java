package com.inefavelstore.bot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.awt.Color;

public class VitrineInteractionListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String customId = event.getComponentId();

        switch (customId) {
            case "btn_vitrine_robux":
                TextInput nickInput = TextInput.create("nick_roblox", "Nick do Roblox", TextInputStyle.SHORT)
                        .setPlaceholder("Seu nick no Roblox...")
                        .setRequired(true)
                        .build();

                TextInput qtdInput = TextInput.create("qtd_robux", "Quantidade de Robux", TextInputStyle.SHORT)
                        .setPlaceholder("Ex: 340")
                        .setRequired(true)
                        .build();

                Modal modalRobux = Modal.create("modal_comprar_robux", "Compra de Robux - Inefavel Store")
                        .addActionRow(nickInput)
                        .addActionRow(qtdInput)
                        .build();

                event.replyModal(modalRobux).queue();
                break;

            case "btn_vitrine_gamepass":
                StringSelectMenu menuJogos = StringSelectMenu.create("select_vitrine_jogos")
                        .setPlaceholder("Selecione um Jogo no Menu:")
                        .addOption("Blox Fruits", "bloxfruits", "Frutas e Gamepasses")
                        .addOption("King Legacy", "kinglegacy", "Itens e Ranks")
                        .build();

                EmbedBuilder embedGp = new EmbedBuilder()
                        .setTitle("Comprar Gamepass / Itens")
                        .setDescription("Selecione o jogo desejado no menu abaixo:")
                        .setColor(new Color(128, 0, 128));

                event.replyEmbeds(embedGp.build())
                        .addActionRow(menuJogos)
                        .setEphemeral(true)
                        .queue();
                break;

            case "btn_vitrine_contas":
                StringSelectMenu menuContas = StringSelectMenu.create("select_vitrine_contas")
                        .setPlaceholder("Selecione a Conta para Comprar:")
                        .addOption("Conta 5k Robux + VIP", "conta_1", "R$ 150,00 - 5000 Robux")
                        .build();

                EmbedBuilder embedContas = new EmbedBuilder()
                        .setTitle("Contas A Venda - Inefavel Store")
                        .setDescription("Selecione no menu abaixo qual conta deseja adquirir:")
                        .setColor(new Color(128, 0, 128));

                event.replyEmbeds(embedContas.build())
                        .addActionRow(menuContas)
                        .setEphemeral(true)
                        .queue();
                break;

            case "btn_vitrine_ver":
                EmbedBuilder embedVitrine = new EmbedBuilder()
                        .setTitle("Vitrine Completa de Produtos - Inefavel Store")
                        .setDescription("Confira nossos itens disponiveis e abra um carrinho para comprar!")
                        .setColor(new Color(128, 0, 128))
                        .setFooter("Abra um ticket pelos botoes acima para comprar!");

                event.replyEmbeds(embedVitrine.build())
                        .setEphemeral(true)
                        .queue();
                break;
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("select_vitrine_jogos")) {
            String jogoSelecionado = event.getValues().get(0);
            event.reply("Voce selecionou o jogo: **" + jogoSelecionado + "**!").setEphemeral(true).queue();
        }
    }
}