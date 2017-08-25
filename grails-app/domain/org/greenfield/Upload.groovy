package org.greenfield

class Upload {
	
	Upload(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String url
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		uuid(nullable:true)
		id generator: 'sequence', params:[sequence:'ID_UPLOAD_PK_SEQ']
    }
}
