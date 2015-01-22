package org.greenfield

class Role {

    String name

    static hasMany = [ accounts: Account, permissions: String ]
    static belongsTo = Account

    static constraints = {
        name(nullable: false, blank: false, unique: true)
		id generator: 'sequence', params:[sequence:'ID_ROLE_PK_SEQ']
    }

}
