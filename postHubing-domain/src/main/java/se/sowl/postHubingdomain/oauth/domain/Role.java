package se.sowl.postHubingdomain.oauth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("USER");

    private final String value;
}
