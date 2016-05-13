package com.mljr.carfinance.common.enums;

public class StaffEnums {
    public static enum Gender {
        MALE(1, "男"), 
        FEMALE(0, "女");
        private String name;
        private int value;
        private Gender(int value, String name) {
            this.name = name;
            this.value = value;
        }
    }

}
