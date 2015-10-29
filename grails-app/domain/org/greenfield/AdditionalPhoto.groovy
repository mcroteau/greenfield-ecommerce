package org.greenfield

class AdditionalPhoto {

	String name
	String imageUrl
	String detailsImageUrl
	
	Date dateCreated
	Date lastUpdated
	
	Product product
	static belongsTo = [ product : Product ]
	
    static constraints = {
		imageUrl(nullable:false)
    }
}
