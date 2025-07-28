package br.com.dinewise.domain.requests.user;

public enum UserTypeEnum {
    RESTAURANT_OWNER("restaurant_owner"),
    CUSTOMER("customer");

    private final String label;

    UserTypeEnum(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
