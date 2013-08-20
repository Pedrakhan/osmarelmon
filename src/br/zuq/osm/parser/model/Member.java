/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.zuq.osm.parser.model;

import java.io.Serializable;

/**
 * 
 * @author Willy Tiengo
 */
public class Member implements Serializable {

	private static final long serialVersionUID = 5420805924993431438L;
	public String type;
	public String ref;
	public String role;

	public Member(String type, String ref, String role) {
		this.type = type;
		this.ref = ref;
		this.role = role;
	}

	// added by sebastians
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Member) {
			return (this.ref.equals(((Member) obj).ref) && this.type
					.equals(((Member) obj).type));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		long id = Long.parseLong(this.ref + this.type);
		return (int) (id % Integer.MAX_VALUE);
	}
}
