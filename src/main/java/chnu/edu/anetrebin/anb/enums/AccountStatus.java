package chnu.edu.anetrebin.anb.enums;

import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("Active account"),
    BLOCKED("Blocked account");

    private final String description;

    AccountStatus(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }
}
