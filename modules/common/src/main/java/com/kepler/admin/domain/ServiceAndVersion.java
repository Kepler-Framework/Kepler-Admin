package com.kepler.admin.domain;

import com.kepler.org.apache.commons.lang.builder.EqualsBuilder;
import com.kepler.org.apache.commons.lang.builder.HashCodeBuilder;
import com.kepler.org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 字符串表示的Service和版本
 * 
 * @author kim 2015年12月16日
 */
public class ServiceAndVersion {

	private final String service;

	private final String versionAndCatalog;

	public ServiceAndVersion(String service, String versionAndCatalog) {
		super();
		this.service = service;
		this.versionAndCatalog = versionAndCatalog;
	}

	public String getService() {
		return this.service;
	}

	public String getVersionAndCatalog() {
		return this.versionAndCatalog;
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(this.getService()).append(this.getVersionAndCatalog()).toHashCode();
	}

	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
