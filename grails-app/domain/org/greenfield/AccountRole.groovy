package org.greenfield

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.apache.commons.lang.builder.HashCodeBuilder

@ToString(cache=true, includeNames=true, includePackage=false)
class AccountRole implements Serializable {

	private static final long serialVersionUID = 1

	Account account
	Role role

	AccountRole(Account u, Role r) {
		this()
		account = u
		role = r
	}

	@Override
	boolean equals(other) {
		if (!(other instanceof AccountRole)) {
			return false
		}

		other.account?.id == account?.id && other.role?.id == role?.id
	}

	@Override
	int hashCode() {
		def builder = new HashCodeBuilder()
		if (account) builder.append(account.id)
		if (role) builder.append(role.id)
		builder.toHashCode()
	}

	static AccountRole get(long accountId, long roleId) {
		criteriaFor(accountId, roleId).get()
	}

	static boolean exists(long accountId, long roleId) {
		criteriaFor(accountId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long accountId, long roleId) {
		AccountRole.where {
			account == Account.load(accountId) &&
			role == Role.load(roleId)
		}
	}

	static AccountRole create(Account account, Role role, boolean flush = false) {
		def instance = new AccountRole(account: account, role: role)
		instance.save(flush: flush, insert: true)
		instance
	}

	static boolean remove(Account u, Role r, boolean flush = false) {
		if (u == null || r == null) return false

		int rowCount = AccountRole.where { account == u && role == r }.deleteAll()

		if (flush) { AccountRole.withSession { it.flush() } }

		rowCount
	}

	static void removeAll(Account u, boolean flush = false) {
		if (u == null) return

		AccountRole.where { account == u }.deleteAll()

		if (flush) { AccountRole.withSession { it.flush() } }
	}

	static void removeAll(Role r, boolean flush = false) {
		if (r == null) return

		AccountRole.where { role == r }.deleteAll()

		if (flush) { AccountRole.withSession { it.flush() } }
	}

	static constraints = {
		role validator: { Role r, AccountRole ur ->
			if (ur.account == null || ur.account.id == null) return
			boolean existing = false
			AccountRole.withNewSession {
				existing = AccountRole.exists(ur.account.id, r.id)
			}
			if (existing) {
				return 'userRole.exists'
			}
		}
	}

	static mapping = {
		id composite: ['account', 'role']
		version false
	}
}
