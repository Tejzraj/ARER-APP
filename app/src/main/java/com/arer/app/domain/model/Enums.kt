package com.arer.app.domain.model

enum class CalculationType {
    PER_STUDENT,
    CUSTOM_COUNT,
    FIXED_MONTHLY
}

enum class HolidayType {
    SUNDAY,
    GOVERNMENT_HOLIDAY,
    CUSTOM_HOLIDAY,
    OVERRIDE
}

enum class DailyEntryStatus {
    NORMAL,
    HOLIDAY,
    GOVERNMENT_HOLIDAY,
    OVERRIDDEN
}

enum class ReportStatus {
    DRAFT,
    READY,
    FINALIZED
}
