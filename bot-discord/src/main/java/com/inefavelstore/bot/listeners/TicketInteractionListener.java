package com.inefavelstore.bot.listeners;

import com.inefavelstore.bot.config.Config;
import com.inefavelstore.bot.services.RobloxService;
import com.inefavelstore.bot.services.TicketService;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ModalSubmitInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.object.entity.User;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.discordjson.possible.Possible;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

public class TicketInteractionListener {

    public static void registrar(GatewayDiscordClient client) {
        client.on(ModalSubmitInteractionEvent.class, event -> {
            String customId = event.getCustomId();

            if ("modal_comprar_robux".equals(customId)) {
                return processarModalRobux(event);
            }

            return Mono.empty();
        }).subscribe();
    }

    private static Mono<Void> processarModalRobux(ModalSubmitInteractionEvent event) {
        // Extrai o valor buscando pelo customId do campo de texto
        String nick = extrairValorCampo(event, "input_nick_roblox", "Nao informado");
        String qtdStr = extrairValorCampo(event, "input_qtd_robux", "0");

        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            return event.reply("Insira apenas numeros na quantidade!").withEphemeral(true);
        }

        double valorTotal = quantidade * Config.PRECO_ROBUX_GAMEPASS;
        User cliente = event.getInteraction().getUser();

        return event.getInteraction().getGuild()
                .flatMap(guild -> TicketService.criarCanalCarrinho(guild, cliente, "carrinho")
                        .flatMap(canalCarrinho -> {
                            String urlSkin = RobloxService.buscarAvatarRoblox(nick);

                            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                                    .title(String.format("Carrinho de Robux - %d Robux", quantidade))
                                    .description(String.format(
                                            "Ola %s!\n\n" +
                                            "Nick do Roblox: %s\n" +
                                            "Quantidade de Robux: %d\n" +
                                            "Taxa por Robux: R$ %.3f\n" +
                                            "Valor Exato a Pagar: R$ %.2f",
                                            cliente.getMention(), nick, quantidade, Config.PRECO_ROBUX_GAMEPASS, valorTotal
                                    ).replace('.', ','))
                                    .color(Color.of(0x800080))
                                    .thumbnail(urlSkin)
                                    .build();

                            ActionRow botoesAcao = ActionRow.of(
                                    Button.primary("btn_pix", "Pegar Chave PIX"),
                                    Button.success("btn_qrcode", "Gerar QR Code"),
                                    Button.danger("btn_fechar", "Fechar Carrinho")
                            );

                            return canalCarrinho.createMessage(MessageCreateSpec.builder()
                                            .addEmbed(embed)
                                            .addComponent(botoesAcao)
                                            .build())
                                    .then(event.reply("Carrinho criado com sucesso em " + canalCarrinho.getMention()).withEphemeral(true));
                        })
                );
    }

    // Método auxiliar seguro para buscar o valor do campo no Discord4J 3.2.x
    private static String extrairValorCampo(ModalSubmitInteractionEvent event, String customId, String valorPadrao) {
        return event.getComponents().stream()
                .filter(row -> row.getData().components().isAbsent() == false)
                .flatMap(row -> row.getData().components().get().stream())
                .filter(comp -> comp.customId().isAbsent() == false && customId.equals(comp.customId().get()))
                .findFirst()
                .flatMap(comp -> {
                    Possible<String> valuePoss = comp.value();
                    if (valuePoss.isAbsent()) return java.util.Optional.empty();
                    return java.util.Optional.ofNullable(valuePoss.get());
                })
                .orElse(valorPadrao);
    }
}