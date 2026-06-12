package com.college.sms.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class GradeUtil {

    private GradeUtil() {}

    public static String calculateGrade(BigDecimal obtained, BigDecimal max) {
        if (max == null || max.compareTo(BigDecimal.ZERO) == 0) return "F";
        BigDecimal pct = obtained.multiply(BigDecimal.valueOf(100))
                .divide(max, 2, RoundingMode.HALF_UP);
        return calculateGradeFromPercentage(pct);
    }

    public static String calculateGradeFromPercentage(BigDecimal percentage) {
        double p = percentage.doubleValue();
        if (p >= 90) return "A+";
        if (p >= 80) return "A";
        if (p >= 70) return "B+";
        if (p >= 60) return "B";
        if (p >= 50) return "C";
        if (p >= 40) return "D";
        return "F";
    }
}
