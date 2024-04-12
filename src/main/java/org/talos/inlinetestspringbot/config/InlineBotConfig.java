package org.talos.inlinetestspringbot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.talos.inlinetestspringbot.bot.InlineBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class InlineBotConfig {
    @Bean
    public TelegramBotsLongPollingApplication telegramBots (
        @Value("${bot.token}") String botToken,
        InlineBot inlineBot
    ) throws TelegramApiException {
        var api = new TelegramBotsLongPollingApplication();
        api.registerBot(botToken, inlineBot);
        return api;
    }
}
