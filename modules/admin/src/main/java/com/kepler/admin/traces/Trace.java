package com.kepler.admin.traces;

/**
 * @author KimShen
 *
 */
public interface Trace {

	public String getDate();

	public String getTrace();

	public String getCause();

	public String getMethod();

	public String getService();

	public String getVersionAndCatalog();

	public String getHost();

	public String getGroup();

	public String getApplication();

}
