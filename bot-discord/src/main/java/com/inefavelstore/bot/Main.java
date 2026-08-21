package com.inefavelstore.bot;

import com.inefavelstore.bot.listeners.*;
import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        iniciarKeepAlive();

        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES
                )
                .setActivity(Activity.streaming("Nezumi | Os melhores produtos", "https://guns.lol/oboz"))
                .addEventListeners(
                        new SlashCommandListener(),
                        new MessageListener(),
                        new TicketInteractionListener(),
                        new VitrineInteractionListener()
                )
                .build();

        jda.awaitReady();

        // Registrar Comandos Slash
        jda.updateCommands().addCommands(
                Commands.slash("store_new", "Cria toda a estrutura de categorias e canais para a sua loja"),
                Commands.slash("ver_vitrine", "Visualiza a vitrine de produtos da loja")
        ).queue();

        System.out.println("Bot Java conectado com sucesso!");
    }

    private static void iniciarKeepAlive() {
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", exchange -> {
                String response = "Bot Java esta online e ativo 24/7!";
                exchange.sendResponseHeaders(200, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            });
            server.start();
            System.out.println("Servidor Keep-Alive rodando na porta: " + port);
        } catch (Exception e) {
            System.out.println("Erro ao iniciar servidor Keep-Alive: " + e.getMessage());
        }
    }
}