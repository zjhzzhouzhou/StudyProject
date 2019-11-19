package com.zz.amqp1.utils.job;

public class Constant {
	/**
	 * 定时任务状态
	 */
	public static class JOB_STATE {
		/** 启用 */
		public static int YES = 1;
		/** 停用 */
		public static int NO = 0;
	}

	/**
	 * 系统响应编码
	 */
	public static class SYSCODE{
		/** 成功 */
		public static String SUCCESS="200";
		/** 失败  */
		public static String FAIL="500";
		/** 校验不通过  */
		public static String CHECKFIAL="204";
	}

	/**
	 * 响应结果
	 */
	public static class RETURN_STATE {
		/** 成功 */
		public static String SUCC = "1";
		/** 失败 */
		public static String FAIL = "0";
	}

}
