package com.cn.common.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class MySqlPaginationInterceptor extends
		AbstractPaginationInterceptor {

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append(sql);
		pagingSelect.append(" LIMIT ");
		pagingSelect.append(offset);
		pagingSelect.append(" , ");
		pagingSelect.append(limit);
		return pagingSelect.toString();
	}
}
