/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.topic;

import java.util.LinkedHashSet;

import org.vsw.nlp.util.StringPool;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Place extends Topic {
	final static public String ENTITY_TYPE = "place" ;

  private String   addressNumber ;
  private String   street ;
  private String   quarter ;
  private String   district ;
  private String   city ;
  private String   province ;
  private String   country ;
  private String   postalCode ;
  private String   description ;

  public Place() {
  }

  public String getOType() { return ENTITY_TYPE ; }
  public void   setOType(String s) { }
  
	public String getAddressNumber() { return addressNumber; }
	public void setAddressNumber(String addressNumber) { this.addressNumber = addressNumber; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getQuarter() { return quarter; }
	public void setQuarter(String quarter) { this.quarter = quarter; }

	public String getDistrict() { return district; }
	public void setDistrict(String district) { this.district = district; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	public String getProvince() { return province;}
	public void setProvince(String province) { this.province = province; }

	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }

	public String getPostalCode() { return postalCode; }
	public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getCityType() {
		String[] TYPE = {"thành phố", "thị xã"} ;
		if(getType() != null) {
			for(String sel : getType()) {
				if(StringUtil.isIn(sel, TYPE)) return sel ;
			}
		}
		return "thành phố" ;
	}
	
	public String getDistrictType() {
		String[] TYPE = {"quận", "huyện", "thị trấn"} ;
		if(getType() != null) {
			for(String sel : getType()) {
				if(StringUtil.isIn(sel, TYPE)) return sel ;
			}
		}
		return null ;
	}
	
	public String getQuarterType() {
		String[] TYPE = {"phường", "xã", "thôn"} ;
		if(getType() != null) {
			for(String sel : getType()) {
				if(StringUtil.isIn(sel, TYPE)) return sel ;
			}
		}
		return null ;
	}
	
	public String getStreetType() {
		String[] TYPE = {"đường", "phố"} ;
		if(getType() != null) {
			for(String sel : getType()) {
				if(StringUtil.isIn(sel, TYPE)) return sel ;
			}
		}
		return null ;
	}
	
	public String getNameType() {
		String[] TYPE = {"đường", "phố", "phường", "xã", "thôn", "quận", "huyện", "thị trấn", "thành phố", "thị xã"} ;
		if(getType() != null) {
			for(String sel : getType()) {
				if(StringUtil.isIn(sel, TYPE)) return sel ;
			}
		}
		return null ;
	}
	
	public String getFullName() {
		StringBuilder b = new StringBuilder() ;
		b.append(getName()) ;
		if(this.district != null) {
			b.append(" - ").append(this.district) ;
		}
		
		if(this.city != null) {
			b.append(" - ").append(this.city) ;
		} else if(this.province != null) {
			b.append(" - ").append(this.province) ;
		}
		if(getType() != null) {
			b.append(" - ") ;
			for(String sel : getType()) {
				b.append(sel).append(", ") ;
			}
		}
		return b.toString() ;
	}
	
	public String getHId() {
		StringBuilder b = new StringBuilder() ;
		appendForHId(b, getName()) ;
		if(this.district != null) {
			b.append('-');
			appendForHId(b, this.district) ;
		}
		
		if(this.city != null) {
			b.append('-');
			appendForHId(b, this.city) ;
		} else if(this.province != null) {
			b.append('-');
			appendForHId(b, this.province) ;
		}
		return b.toString() ;
	}
	
	public void optimize(StringPool pool) {
		super.optimize(pool) ;
	  street = pool.getString(street);
	  quarter = pool.getString(quarter);
	  district = pool.getString(district);
	  city = pool.getString(city);
	  province = pool.getString(province);
	  country = pool.getString(country);
	}

	public String[] getPath() {
	  LinkedHashSet<String> holder = new LinkedHashSet<String>() ;
		if(country != null) holder.add(country) ;
		if(province != null) holder.add(province) ;
		if(city != null) holder.add(city) ;
		if(district != null) holder.add(district) ;
		if(quarter != null) holder.add(quarter) ;
		if(street != null) holder.add(street) ;
		if(getName() != null) holder.add(getName()) ;
	  return holder.toArray(new String[holder.size()]) ;
	}
	
	private void appendForHId(StringBuilder b, String string) {
		for(int i = 0; i < string.length(); i++) {
			char c = string.charAt(i) ;
			if(c == ' ') {
				b.append('.') ;
			} else {
				b.append(c) ;
			}
		}
	}
}