package com.kepler.admin.resource.instance.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import com.kepler.admin.domain.ServiceAndVersion;
import com.kepler.admin.resource.instance.Instance;

/**
 * 从Instance去重Service + Version
 * 
 * @author kim
 *
 * 2016年3月8日
 */
public class InstanceServices extends TreeSet<ServiceAndVersion> {

	private static final ServiceComparator COMPARATOR = new ServiceComparator();

	private static final long serialVersionUID = 1L;

	public InstanceServices(Collection<Instance> instances) {
		super(InstanceServices.COMPARATOR);
		if (instances != null) {
			for (Instance each : instances) {
				super.add(each.getService());
			}
		}
	}

	private static class ServiceComparator implements Comparator<ServiceAndVersion> {

		@Override
		public int compare(ServiceAndVersion o1, ServiceAndVersion o2) {
			return String.CASE_INSENSITIVE_ORDER.compare(o1.getService() + o1.getVersionAndCatalog(), o2.getService() + o2.getVersionAndCatalog());
		}
	}
}