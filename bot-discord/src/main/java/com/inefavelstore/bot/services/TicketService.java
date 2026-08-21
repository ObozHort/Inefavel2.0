package com.inefavelstore.bot.services;

import com.inefavelstore.bot.config.Config;
import discord4j.common.util.Snowflake;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.Category;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.core.spec.TextChannelCreateSpec;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

public class TicketService {

    public static Set<PermissionOverwrite> obterPermissoesCanal(Guild guild, User cliente) {
        Set<PermissionOverwrite> overwrites = new HashSet<>();

        // Nega leitura ao cargo padrao (@everyone)
        overwrites.add(PermissionOverwrite.forRole(
                guild.getId(),
                PermissionSet.none(),
                PermissionSet.of(Permission.VIEW_CHANNEL)
        ));

        // Permite o cliente visualizar e interagir
        overwrites.add(PermissionOverwrite.forMember(
                cliente.getId(),
                PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES, Permission.READ_MESSAGE_HISTORY),
                PermissionSet.none()
        ));

        // Permite permissao para o bot
        overwrites.add(PermissionOverwrite.forMember(
                guild.getClient().getSelfId(),
                PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES, Permission.MANAGE_CHANNELS),
                PermissionSet.none()
        ));

        // Adiciona permissao para Staff se configurado
        if (Config.ID_CARGO_STAFF != 0) {
            overwrites.add(PermissionOverwrite.forRole(
                    Snowflake.of(Config.ID_CARGO_STAFF),
                    PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES, Permission.READ_MESSAGE_HISTORY),
                    PermissionSet.none()
            ));
        }

        return overwrites;
    }

    public static Mono<TextChannel> criarCanalCarrinho(Guild guild, User cliente, String prefixo) {
        String nomeCanal = prefixo + "-" + cliente.getUsername().toLowerCase();

        return Mono.justOrEmpty(Config.ID_CATEGORIA_CARRINHOS != 0 ? Snowflake.of(Config.ID_CATEGORIA_CARRINHOS) : null)
                .flatMap(guild::getChannelById)
                .ofType(Category.class)
                .flatMap(categoria -> guild.createTextChannel(TextChannelCreateSpec.builder()
                        .name(nomeCanal)
                        .parentId(categoria.getId())
                        .permissionOverwrites(obterPermissoesCanal(guild, cliente))
                        .build()))
                .switchIfEmpty(guild.createTextChannel(TextChannelCreateSpec.builder()
                        .name(nomeCanal)
                        .permissionOverwrites(obterPermissoesCanal(guild, cliente))
                        .build()));
    }
}