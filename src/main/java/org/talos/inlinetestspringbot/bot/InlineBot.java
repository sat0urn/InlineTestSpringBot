package org.talos.inlinetestspringbot.bot;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.talos.inlinetestspringbot.service.UserService;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Component
public class InlineBot implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final UserService userService;

    @Autowired
    public InlineBot(@Value("${bot.token}") String botToken, UserService userService) {
        telegramClient = new OkHttpTelegramClient(botToken);
        this.userService = userService;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();
            long user_id = update.getMessage().getChat().getId();

            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if (message.equals("/start")) {
                String answer = EmojiParser.parseToUnicode(
                        """
                                Добро пожаловать сюда!
                                Надеюсь мы с вами сочтемся и
                                найдем общий язык.
                                :sparkles::sparkles::sparkles:
                                """
                );

                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(answer)
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                try {
                    telegramClient.execute(sendMessage);
                    userService.check(user_first_name, user_last_name, (int)user_id, user_username);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("/pictures")) {
                String answer = EmojiParser.parseToUnicode("My favorite pics :smile:");

                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(answer)
                        .build();

                sendMessage.setReplyMarkup(
                        ReplyKeyboardMarkup
                                .builder()
                                .keyboardRow(new KeyboardRow("#Cat", "#Man"))
                                .keyboardRow(new KeyboardRow("#Birds", "#More_birds"))
                                .build()
                );

                log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("#Cat")) {
                String caption = EmojiParser.parseToUnicode("Cat :cat:");

                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .chatId(chat_id)
                        .photo(new InputFile("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTEuMzkuNTkucG5n.png"))
                        .caption(caption)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Man")
                                                .callbackData("#Man")
                                                .build())
                                )
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Birds")
                                                .callbackData("#Birds")
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("More birds")
                                                .callbackData("#More_birds")
                                                .build())
                                )
                                .build()
                        )
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                try {
                    telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("#Man")) {
                String caption = EmojiParser.parseToUnicode("Man :man:");

                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .chatId(chat_id)
                        .photo(new InputFile("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTIuNTMuMTkucG5n.png"))
                        .caption(caption)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Cat")
                                                .callbackData("#Cat")
                                                .build())
                                )
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Birds")
                                                .callbackData("#Birds")
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("More birds")
                                                .callbackData("#More_birds")
                                                .build())
                                )
                                .build()
                        )
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                try {
                    telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("#Birds")) {
                String caption = EmojiParser.parseToUnicode("Birds :bird:");

                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .chatId(chat_id)
                        .photo(new InputFile("https://assets.rawpixel.com/image_600/dG9waWNzL2ltYWdlcy9kZXNpZ24tMTY5Njk4NjcxMDA3NC0xLmpwZw.jpg"))
                        .caption(caption)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Cat")
                                                .callbackData("#Cat")
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Man")
                                                .callbackData("#Man")
                                                .build())
                                )
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("More birds")
                                                .callbackData("#More_birds")
                                                .build())
                                )
                                .build()
                        )
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                try {
                    telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("#More_birds")) {
                String caption = EmojiParser.parseToUnicode("More birds :bird::bird::bird:");

                SendPhoto sendPhoto = SendPhoto
                        .builder()
                        .chatId(chat_id)
                        .photo(new InputFile("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTguNTUuMzAucG5n.png"))
                        .caption(caption)
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Cat")
                                                .callbackData("#Cat")
                                                .build(),
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Man")
                                                .callbackData("#Man")
                                                .build())
                                )
                                .keyboardRow(new InlineKeyboardRow(
                                        InlineKeyboardButton
                                                .builder()
                                                .text("Birds")
                                                .callbackData("#Birds")
                                                .build())
                                )
                                .build()
                        )
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

                try {
                    telegramClient.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (message.equals("/hide")) {
                String answer = EmojiParser.parseToUnicode("No pictures anymore :angry:");

                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(answer)
                        .build();

                sendMessage.setReplyMarkup(ReplyKeyboardRemove.builder().build());

                log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                String answer = "I don't know what you want from me!\n\nYou: " + message;

                SendMessage sendMessage = SendMessage
                        .builder()
                        .chatId(chat_id)
                        .text(answer)
                        .build();

                log(user_first_name, user_last_name, Long.toString(user_id), message, answer);

                try {
                    telegramClient.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasMessage() && update.getMessage().hasPhoto()) {
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            long user_id = update.getMessage().getChat().getId();

            String message = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            List<PhotoSize> photos = update.getMessage().getPhoto();

            String f_id = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .map(PhotoSize::getFileId)
                    .orElse("");
            int f_width = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .map(PhotoSize::getWidth)
                    .orElse(0);
            int f_height = photos.stream()
                    .max(Comparator.comparing(PhotoSize::getFileSize))
                    .map(PhotoSize::getHeight)
                    .orElse(0);

            String caption = EmojiParser.parseToUnicode(
                    String.format("""
                            :camera::camera::camera:
                            Id: %s
                            Width: %d
                            Height: %d
                            """, f_id, f_width, f_height)
            );

            SendPhoto sendPhoto = SendPhoto
                    .builder()
                    .chatId(chat_id)
                    .photo(new InputFile(f_id))
                    .caption(caption)
                    .build();

            log(user_first_name, user_last_name, Long.toString(user_id), message, caption);

            try {
                telegramClient.execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            int message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("#Cat")) {
                EditMessageMedia new_photo = EditMessageMedia
                        .builder()
                        .chatId(chat_id)
                        .messageId(message_id)
                        .media(new InputMediaPhoto("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTEuMzkuNTkucG5n.png"))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Man")
                                                        .callbackData("#Man")
                                                        .build()
                                        )
                                )
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Birds")
                                                        .callbackData("#Birds")
                                                        .build(),
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("More birds")
                                                        .callbackData("#More_birds")
                                                        .build()
                                        )
                                )
                                .build()
                        )
                        .build();

                try {
                    telegramClient.execute(new_photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.equals("#Man")) {
                EditMessageMedia new_photo = EditMessageMedia
                        .builder()
                        .chatId(chat_id)
                        .messageId(message_id)
                        .media(new InputMediaPhoto("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTIuNTMuMTkucG5n.png"))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Cat")
                                                        .callbackData("#Cat")
                                                        .build()
                                        )
                                )
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Birds")
                                                        .callbackData("#Birds")
                                                        .build(),
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("More birds")
                                                        .callbackData("#More_birds")
                                                        .build()
                                        )
                                )
                                .build()
                        )
                        .build();

                try {
                    telegramClient.execute(new_photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.equals("#Birds")) {
                EditMessageMedia new_photo = EditMessageMedia
                        .builder()
                        .chatId(chat_id)
                        .messageId(message_id)
                        .media(new InputMediaPhoto("https://assets.rawpixel.com/image_600/dG9waWNzL2ltYWdlcy9kZXNpZ24tMTY5Njk4NjcxMDA3NC0xLmpwZw.jpg"))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Cat")
                                                        .callbackData("#Cat")
                                                        .build(),
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Man")
                                                        .callbackData("#Man")
                                                        .build()
                                        )
                                )
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("More birds")
                                                        .callbackData("#More_birds")
                                                        .build()
                                        )
                                )
                                .build()
                        )
                        .build();

                try {
                    telegramClient.execute(new_photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if (call_data.equals("#More_birds")) {
                EditMessageMedia new_photo = EditMessageMedia
                        .builder()
                        .chatId(chat_id)
                        .messageId(message_id)
                        .media(new InputMediaPhoto("https://assets.rawpixel.com/image_png_600/dG9waWNzL2ltYWdlcy9zY3JlZW5zaG90LTIwMjEtMDktMDEtYXQtMTguNTUuMzAucG5n.png"))
                        .replyMarkup(InlineKeyboardMarkup
                                .builder()
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Cat")
                                                        .callbackData("#Cat")
                                                        .build(),
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Man")
                                                        .callbackData("#Man")
                                                        .build()
                                        )
                                )
                                .keyboardRow(
                                        new InlineKeyboardRow(
                                                InlineKeyboardButton
                                                        .builder()
                                                        .text("Birds")
                                                        .callbackData("#Birds")
                                                        .build()
                                        )
                                )
                                .build()
                        )
                        .build();

                try {
                    telegramClient.execute(new_photo);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void log(
            String first_name,
            String last_name,
            String user_id,
            String text,
            String bot_answer) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        System.out.printf("""
                %s
                -----------------------------------
                Message from %s %s. (id = %s)
                 Text - %s
                Bot answer:
                 Text - %s
                """, dateFormat.format(date), first_name, last_name, user_id, text, bot_answer);
    }
}
