package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.fasterxml.jackson.annotation.JsonCreator;

@Getter
@AllArgsConstructor
public enum AuditTagEnum {
    SEXUAL("sexual"),
    HATE("hate"),
    HARASSMENT("harassment"),
    SELFHARM("selfharm"),
    SEXUAL_MINORS("sexual_minors"),
    HATE_THREATENING("hate_threatening"),
    VIOLENCE_GRAPHIC("violence_graphic"),
    SELFHARM_INTENT("selfharm_intent"),
    SELFHARM_INSTRUCTIONS("selfharm_instructions"),
    HARASSMENT_THREATENING("harassment_threatening"),
    VIOLENCE("violence");

    private String tag;

    @JsonCreator
    public static AuditTagEnum parseOfNullable(String tag) {
        for (AuditTagEnum item : values()) {
            if (item.getTag().equals(tag)) {
                return item;
            }
        }
        return null;
    }
}
