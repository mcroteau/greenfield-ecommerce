package org.greenfield

import java.io.InputStream
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat

import groovy.time.TimeDuration
import groovy.time.TimeCategory

class DataBackupJob {

	def BACKUP_FILE = "backup/greenfield-backup.json"
	
	def exportDataService
	
    static triggers = {
      	//simple startDelay: 60000 * 7, repeatInterval: 60000 * 73//TODO:backup needs to be refactored, clear logs
    }

    void execute() {
		def sdf = new SimpleDateFormat("dd MMM - hh:mm:ssa")
		
		def startDate = new Date()
		def startDateTime = sdf.format(startDate)
		
		println "**********************************************************"
		println "             Data Backup ${startDateTime}                 "
		println "**********************************************************"
		
		
		def params = createParametersMap()
		def json = exportDataService.export(params)

		InputStream is = new ByteArrayInputStream(json.getBytes());
		def backupFile = grailsApplication.mainContext.getResource(BACKUP_FILE).file

		backupFile.write(json)
		
		def endDate = new Date()
		def endDateTime = sdf.format(endDate)
		
		TimeDuration duration = TimeCategory.minus(endDate, startDate)
		
		println "**********************************************************"
		println "           Backup Complete ${endDateTime}                 "
		println "           Total Time : ${duration}                       "
		println "**********************************************************"
    }
	
	
	def createParametersMap(){
		def params = [:]
		
		params['exportAccounts'] = "on"
		params['exportCatalogs'] = "on"
		params['exportProducts'] = "on"
		params['exportProductOptions'] = "on"
		params['exportSpecifications'] = "on"
		params['exportAdditionalPhotos'] = "on"
		params['exportShoppingCarts'] = "on"
		params['exportOrders'] = "on"
		params['exportPages'] = "on"
		params['exportUploads'] = "on"
		params['exportLayout'] = "on"
		params['exportLogs'] = "on"
		
		return params
	}
}
