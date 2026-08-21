package com.inefavelstore.bot.listeners;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class ModalListener extends ListenerAdapter {

    // Método auxiliar para criar o Modal de Robux (equivalente ao FormularioRobux em Python)
    public static Modal criarModalRobux() {
        TextInput nickInput = TextInput.create("nick_roblox", "Nick do Roblox", TextInputStyle.SHORT)
                .setPlaceholder("Seu nome no Roblox...")
                .setRequired(true)
                .build();

        TextInput qtdInput = TextInput.create("qtd_robux", "Quantidade de Robux", TextInputStyle.SHORT)
                .setPlaceholder("Ex: 340")
                .setRequired(true)
                .build();

        return Modal.create("modal_compra_robux", "Compra de Robux • Inefavel Store")
                .addActionRow(nickInput)
                .addActionRow(qtdInput)
                .build();
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("modal_compra_robux")) {
            var nickMapping = event.getValue("nick_roblox");
            var qtdMapping = event.getValue("qtd_robux");

            if (nickMapping == null || qtdMapping == null) {
                event.reply(" Erro ao processar os campos do formulário.").setEphemeral(true).queue();
                return;
            }

            String nick = nickMapping.getAsString();
            String qtdStr = qtdMapping.getAsString();

            try {
                int qtd = Integer.parseInt(qtdStr);
                double precoTotal = qtd * 0.035; // PRECO_ROBUX_GAMEPASS

                event.deferReply(true).queue();

                // Monta a mensagem utilizando nick, qtd e precoTotal
                String mensagem = String.format(" Carrinho criado com sucesso para **%s**!\n **Quantidade:** %d Robux\n **Total:** R$ %.2f", 
                        nick, qtd, precoTotal);

                // Lógica de criação de canal de carrinho e envio do Embed com botões virá aqui...
                event.getHook().sendMessage(mensagem).setEphemeral(true).queue();

            } catch (NumberFormatException e) {
                event.reply(" Insira apenas números válidos na quantidade!").setEphemeral(true).queue();
            }
        }
    }
}