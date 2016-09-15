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
import ch.qos.logback.core.FileAppender

import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %highlight(%-5level) %red(%logger{10}) %cyan(%M) - %msg%n" }
}

appender("FILE", FileAppender) {
	file = "./logs/chameleon-test.log"
    append = false
//    encoder(PatternLayoutEncoder) { pattern = "%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" }
    encoder(PatternLayoutEncoder) { pattern = "%-5level %logger{36} - %msg%n" }
}

/*appender("FILE", RollingFileAppender) {
	file = "./logs/chameleon.log"
	encoder(PatternLayoutEncoder) { pattern = "%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" }
	rollingPolicy(FixedWindowRollingPolicy) {
		fileNamePattern = "./logs/chameleon.%i.log.zip"
		minIndex = 1
		maxIndex = 10
	}
	triggeringPolicy(SizeBasedTriggeringPolicy) { maxFileSize = "2MB" }
}*/

// benchmarking
logger("benchmarking.workload.transaction", INFO)

// context
logger("client.context", WARN)
logger("slave.context", WARN)
logger("master.context", WARN)

// table
logger("kvs.table.AbstractTable", WARN)
logger("kvs.table.MultiTimestampedCellsStore", INFO)

// client
logger("client.clientlibrary.rvsi.vc", DEBUG)
logger("client.clientlibrary.transaction.RVSITransaction", INFO)
logger("client.clientlibrary.transaction.BufferedUpdates", DEBUG)

// master
logger("master.SIMaster", WARN)

// slave


// coordinator
logger("twopc.coordinator", WARN)
logger("twopc.coordinator.phaser.CommitPhaser", INFO)

// timestamp oracle
logger("timing.CentralizedTimestampOracle", INFO)

// socket
logger("messaging.socket", INFO)

// rmi
logger("rmi", ERROR)

// util
logger("util", ERROR)

root(DEBUG, ["STDOUT", "FILE"])