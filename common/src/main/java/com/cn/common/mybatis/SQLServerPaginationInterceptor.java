package com.cn.common.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.util.Locale;

/**
 * 分页支持：支持SQL Server2005及以上。
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) })
public class SQLServerPaginationInterceptor extends
		AbstractPaginationInterceptor {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		int orderByIndex = sql.toLowerCase(Locale.ENGLISH).lastIndexOf(
				"order by");

		if (orderByIndex <= 0) {
			int selectIndex = sql.toLowerCase(Locale.ENGLISH).lastIndexOf(
					"select");
			return new StringBuffer(sql.length() + 100)
					.append("with tempPagination as(")
					.append(sql)
					.insert(selectIndex + 29,
							" ROW_NUMBER() OVER(ORDER BY " + "getdate() desc"
									+ ") as RowNumber,")
					.append(") select * from tempPagination where RowNumber between "
							+ (offset + 1) + " and " + (offset + limit))
					.toString();
		} else {
			String sqlOrderBy = sql.substring(orderByIndex + 8);
			String sqlRemoveOrderBy = sql.substring(0, orderByIndex);
			int insertPoint = getSqlAfterSelectInsertPoint(sql);
			return new StringBuffer(sql.length() + 100)
					.append("with tempPagination as(")
					.append(sqlRemoveOrderBy)
					.insert(insertPoint + 23,
							" ROW_NUMBER() OVER(ORDER BY " + sqlOrderBy
									+ ") as RowNumber,")
					.append(") select * from tempPagination where RowNumber between "
							+ (offset + 1) + " and " + (offset + limit))
					.toString();
		}
	}

	/**
	 * 获取sql中select子句位置
	 */
	protected int getSqlAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase(Locale.ENGLISH).indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase(Locale.ENGLISH)
				.indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

}
