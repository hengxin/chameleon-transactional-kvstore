//
// Built on Fri Oct 30 08:26:15 UTC 2015 by logback-translator
// For more information on configuration files in Groovy
// please see http://logback.qos.ch/manual/groovy.html

// For assistance related to this tool or configuration files
// in general, please contact the logback user mailing list at
//    http://qos.ch/mailman/listinfo/logback-user

// For professional support please see
//   http://www.qos.ch/shop/products/professionalSupport

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import static ch.qos.logback.classic.Level.DEBUG
import static ch.qos.logback.classic.Level.INFO

appender("STDOUT", ConsoleAppender) {
	encoder(PatternLayoutEncoder) { pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n" }
}

appender("FILE", RollingFileAppender) {
	file = "./logs/chameleon.log"
	encoder(PatternLayoutEncoder) { pattern = "%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" }
	rollingPolicy(FixedWindowRollingPolicy) {
		fileNamePattern = "./logs/chameleon.%i.log.zip"
		minIndex = 1
		maxIndex = 10
	}
	triggeringPolicy(SizeBasedTriggeringPolicy) { maxFileSize = "2MB" }
}

//logger("client", INFO, ["STDOUT", "FILE"], false)
root(DEBUG, ["STDOUT", "FILE"])