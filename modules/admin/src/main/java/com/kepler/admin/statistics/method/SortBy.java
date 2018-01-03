package com.kepler.admin.statistics.method;

import java.util.Comparator;

/**
 * 排序器
 * 
 * @author longyaokun 2016年7月12日
 */
public enum SortBy {

	RTT, TOTAL, EXCEPTION, TIMEOUT;

	/**
	 * 获取排序器
	 * 
	 * @return
	 */
	public Comparator<MethodInvoker> comparator() {
		switch (this) {
		case RTT:
			return RttComparator.INSTANCE;
		case TOTAL:
			return TotalComparator.INSTANCE;
		case TIMEOUT:
			return TimeoutComparator.INSTANCE;
		case EXCEPTION:
			return ExceptionComparator.INSTANCE;
		default:
			return RttComparator.INSTANCE;
		}
	}

	/**
	 * RTT排序
	 * 
	 * @author KimShen
	 *
	 */
	private static class RttComparator implements Comparator<MethodInvoker> {

		public static final RttComparator INSTANCE = new RttComparator();

		private RttComparator() {

		}

		@Override
		public int compare(MethodInvoker o1, MethodInvoker o2) {
			return o2.getRtt() - o1.getRtt() > 0 ? 1 : -1;
		}
	}

	/**
	 * 在访问量排序
	 * 
	 * @author KimShen
	 *
	 */
	private static class TotalComparator implements Comparator<MethodInvoker> {

		public static final TotalComparator INSTANCE = new TotalComparator();

		private TotalComparator() {

		}

		@Override
		public int compare(MethodInvoker o1, MethodInvoker o2) {
			return o2.getTotal() - o1.getTotal() > 0 ? 1 : -1;
		}
	}

	/**
	 * 超时排序
	 * 
	 * @author KimShen
	 *
	 */
	private static class TimeoutComparator implements Comparator<MethodInvoker> {

		public static final TimeoutComparator INSTANCE = new TimeoutComparator();

		private TimeoutComparator() {

		}

		@Override
		public int compare(MethodInvoker o1, MethodInvoker o2) {
			return o2.getTimeout() - o1.getTimeout() > 0 ? 1 : -1;
		}
	}

	/**
	 * 异常排序
	 * 
	 * @author KimShen
	 *
	 */
	private static class ExceptionComparator implements Comparator<MethodInvoker> {

		public static final ExceptionComparator INSTANCE = new ExceptionComparator();

		private ExceptionComparator() {

		}

		@Override
		public int compare(MethodInvoker o1, MethodInvoker o2) {
			return o2.getException() - o1.getException() > 0 ? 1 : -1;
		}
	}
}
