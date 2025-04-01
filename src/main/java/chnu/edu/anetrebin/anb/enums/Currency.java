package chnu.edu.anetrebin.anb.enums;

import lombok.Getter;

@Getter
public enum Currency {
    USD("USD", "$", "US Dollar"),
    EUR("EUR", "€", "Euro"),
    UAH("UAH", "₴", "Ukrainian Hryvnia");

    private final String code;
    private final String symbol;
    private final String displayName;

    Currency(String code, String symbol, String displayName) {
        this.code = code;
        this.symbol = symbol;
        this.displayName = displayName;
    }
}
