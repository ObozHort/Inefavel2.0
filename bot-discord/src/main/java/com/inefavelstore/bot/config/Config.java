package com.inefavelstore.bot.config;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public static final String NOME_LOJA = "Inefavel Store";
    public static final String LOGO_LOJA_URL = "https://cdn.discordapp.com/attachments/123456789/123456789/logo.png";
    public static final String DISCORD_INVITE = "https://discord.gg/GW3zkWtZbN";
    public static final String STREAM_URL = "https://guns.lol/links";

    public static long ID_DONO_OU_CANAL_NOTIFICACAO_COMISSAO = 1539372708477931520L;
    public static long ID_CARGO_STAFF = 0L;
    public static long ID_CARGO_VENDEDOR_PIX = 0L;
    
    public static long ID_CANAL_LOGS_VENDAS = 0L;
    public static long ID_CANAL_CALCULADORA = 0L;
    public static long ID_CATEGORIA_CARRINHOS = 0L;
    public static long ID_CANAL_AVALIACOES = 0L;

    public static boolean CARRINHO_ATIVO = true;
    public static boolean STORE_NEW_ATIVO = true;
    public static boolean BOTOES_CALCULADORA_ATIVOS = false;

    public static double PRECO_ROBUX_GAMEPASS = 0.035;

    public static String CHAVE_PIX_VENDEDOR = "";
    public static String QR_CODE_VENDEDOR_URL = "";

    public static Map<String, Boolean> STATUS_BOTOES_TICKET = new HashMap<>() {{
        put("robux", true);
        put("gamepass", false);
        put("contas", false);
    }};
}