package com.kepler.admin.generic.impl;

import org.springframework.util.Assert;

import com.kepler.admin.generic.GenericRequest;
import com.kepler.service.Service;

/**
 * 接收Request
 * 
 * @author KimShen
 *
 */
public class DefaultRequest implements GenericRequest {

	private String tag;

	private String method;

	private String service;

	private String version;

	private String catalog;

	private Object[] datas;

	private String[] classes;

	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMethod() {
		Assert.notNull(this.method, "Method can not be null");
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getService() {
		Assert.notNull(this.service, "Service can not be null");
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		Assert.notNull(this.version, "Version can not be null");
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCatalog() {
		Assert.notNull(this.catalog, "Catalog can not be null");
		return this.catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public Object[] getDatas() {
		return this.datas;
	}

	public void setDatas(Object[] datas) {
		this.datas = datas;
	}

	public String[] getClasses() {
		return this.classes;
	}

	public void setClasses(String[] classes) {
		this.classes = classes;
	}

	public Service metadata() {
		return new Service(this.getService(), this.getVersion(), this.getCatalog());
	}
}
