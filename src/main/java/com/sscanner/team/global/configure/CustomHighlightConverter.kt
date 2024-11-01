package com.sscanner.team.global.configure

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.pattern.color.ANSIConstants
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase

/**
 * logback-spring 로그 레벨별 색상 지정 Custom Converter Class
 */
class CustomHighlightConverter : ForegroundCompositeConverterBase<ILoggingEvent>() {
    override fun getForegroundColorCode(event: ILoggingEvent): String {
        return when (event.level.toString()) {
            "ERROR" -> ANSIConstants.BOLD + ANSIConstants.RED_FG
            "WARN" -> ANSIConstants.BOLD + ANSIConstants.YELLOW_FG
            "INFO" -> ANSIConstants.BOLD + ANSIConstants.GREEN_FG
            "DEBUG" -> ANSIConstants.BOLD + ANSIConstants.BLUE_FG
            "TRACE" -> ANSIConstants.BLUE_FG
            else -> ANSIConstants.DEFAULT_FG
        }
    }
}
