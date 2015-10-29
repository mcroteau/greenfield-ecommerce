package org.greenfield

class Upload {

	String url
	
	Date dateCreated
	Date lastUpdated

    static constraints = {
		id generator: 'sequence', params:[sequence:'ID_UPLOAD_PK_SEQ']
    }
}
