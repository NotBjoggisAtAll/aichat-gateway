package com.bjoggis.chat.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "bjoggis.aichat")
@Validated
public record AiChatProperties(@NotNull String uiUrl, @NotNull String openAiUrl, @NotNull String monoUrl) {

}
