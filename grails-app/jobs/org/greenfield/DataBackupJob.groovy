package org.greenfield

import java.io.InputStream
import java.io.ByteArrayInputStream

class DataBackupJob {

	def BACKUP_FILE = "backup/greenfield-backup.json"
	
	def exportDataService
	
    static triggers = {
      	simple startDelay: 60000 * 5, repeatInterval: 30000
    }

    void execute() {
		println "execute data backup..."
		def params = createParametersMap()
		def json = exportDataService.export(params)

		InputStream is = new ByteArrayInputStream(json.getBytes());
		def backupFile = grailsApplication.mainContext.getResource(BACKUP_FILE).file

		backupFile.write(json)
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
