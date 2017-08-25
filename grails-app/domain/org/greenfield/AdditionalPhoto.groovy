package org.greenfield

class AdditionalPhoto {
	
	AdditionalPhoto(){
		this.uuid = UUID.randomUUID().toString()
	}
	
	String uuid

	String name
	String imageUrl
	String detailsImageUrl
	
	Date dateCreated
	Date lastUpdated
	
	Product product
	static belongsTo = [ product : Product ]
	
    static constraints = {
		uuid(nullable:true)
		imageUrl(nullable:false)
    }
}
