package com.parking.app.util;

public final class BillCalculatorUtil {

    private BillCalculatorUtil() {
    }

    public static Double calculateBill(
            Long parkedMinutes) {

        if (parkedMinutes == null ||
                parkedMinutes <= 0) {
            return 0.0;
        }

        if (parkedMinutes <= 60) {
            return 20.0;
        }

        return 20.0 +
                ((parkedMinutes - 60) * 0.50);
    }
}
