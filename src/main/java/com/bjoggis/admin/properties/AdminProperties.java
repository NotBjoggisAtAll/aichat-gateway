package com.bjoggis.admin.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "bjoggis.admin")
@Validated
public record AdminProperties(@NotNull String uiUrl) {

}
