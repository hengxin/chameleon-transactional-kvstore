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
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

import static ch.qos.logback.classic.Level.ERROR
import static ch.qos.logback.classic.Level.INFO

// for back up
//    encoder(PatternLayoutEncoder) { pattern = "%d{yyyy-MM-dd_HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n" }

appender("STDOUT", ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %highlight(%-5level) %red(%logger{10}) %cyan(%M) - %msg%n" }
}

appender("FILE", FileAppender) {
	file = "./logs/chameleon-test.log"
    append = false
    encoder(PatternLayoutEncoder) { pattern = "%msg%n" }
}


appender("TO", FileAppender) {
    file = "./logs/chameleon-test-to.log"
    append = false
    encoder(PatternLayoutEncoder) { pattern = "%msg%n" }
}

appender("COORD", FileAppender) {
    file = "./logs/chameleon-test-coord.log"
    append = false
    encoder(PatternLayoutEncoder) { pattern = "%msg%n" }
}

appender("MASTER", FileAppender) {
    file = "./logs/chameleon-test-master.log"
    append = false
    encoder(PatternLayoutEncoder) { pattern = "%msg%n" }
}

appender("FILE", RollingFileAppender) {
	file = "./logs/chameleon-aliyun-batch.log"
    append = true
    encoder(PatternLayoutEncoder) { pattern = "%msg%n" }
	rollingPolicy(FixedWindowRollingPolicy) {
		fileNamePattern = "./logs/chameleon-aliyun-batch.%i.log.zip"
		minIndex = 1
		maxIndex = 100
	}
	triggeringPolicy(SizeBasedTriggeringPolicy) { maxFileSize = "2MB" }
}

// benchmarking
logger("benchmarking.workload.transaction", INFO)
logger("benchmarking.executor.client.ClientExecutor", INFO)

// context
logger("client.context", INFO)
logger("slave.context", INFO)
logger("master.context", INFO)

// table
logger("kvs.table.AbstractTable", INFO)
logger("kvs.table.MultiTimestampedCellsStore", INFO)

// client
logger("client.clientlibrary.rvsi.vc", INFO)
logger("client.clientlibrary.transaction.RVSITransaction", INFO)
logger("client.clientlibrary.transaction.BufferedUpdates", INFO)

// master
logger("master.SIMaster", INFO, ["FILE", "MASTER"])

// slave

// coordinator
logger("twopc.coordinator.phaser.CommitPhaser", INFO)
//logger("twopc.coordinator.phaser.RVSI2PCPhaserCoordinator", INFO, ["COORD"])

// timestamp oracle
//logger("timing.CentralizedTimestampOracle", INFO, ["TO"])

// socket
logger("messaging.socket", INFO)

// rmi
logger("rmi", ERROR)

// util
logger("util", ERROR)

root(INFO, ["STDOUT", "FILE"])