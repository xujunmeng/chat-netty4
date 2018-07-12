package com.cn.common.mybatis;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Properties;

public abstract class AbstractPaginationInterceptor implements Interceptor {

	private Logger log = LoggerFactory
			.getLogger(AbstractPaginationInterceptor.class);
	
	private final static String defaultPageSQLID = "^(get|select|query|search).*$";
	private String pageSQLId;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation
				.getTarget();
		ObjectFactory objectFactory = new DefaultObjectFactory();
		ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, objectFactory, objectWrapperFactory, new DefaultReflectorFactory());
		BoundSql boundSql = (BoundSql) metaStatementHandler
				.getValue("delegate.boundSql");
		Object parameterObject = boundSql.getParameterObject();
		
		if (StringUtils.isEmpty(pageSQLId)) {
			pageSQLId = defaultPageSQLID;
        }
		
		MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
		String id = mappedStatement.getId();
		int index = id.lastIndexOf(".") ;
		if(index>0){
			id = id.substring(index+1);
		}
		// 只重写需要分页的sql语句。通过MappedStatement的ID匹配
        if (!id.matches(pageSQLId)) {
        	return invocation.proceed();
        }
        
		// 判断是否需要分页操作
		if (parameterObject instanceof PagingCriteria && 
				((PagingCriteria)parameterObject).isPaging()) {
			String originalSql = (String) metaStatementHandler
					.getValue("delegate.boundSql.sql");
			PagingCriteria criteria = (PagingCriteria) parameterObject;
			String pageSQL = getLimitString(originalSql, criteria.getPageIndex()
					* criteria.getPageSize(), criteria.getPageSize());
			metaStatementHandler.setValue("delegate.boundSql.sql", pageSQL);
			metaStatementHandler.setValue("delegate.rowBounds.offset",
					RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit",
					RowBounds.NO_ROW_LIMIT);

			log.debug("生成分页SQL : " + boundSql.getSql());

			return invocation.proceed();
		} else {
			return invocation.proceed();
		}
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}
	
	public void setPageSQLId(String pageSQLId) {
		this.pageSQLId = pageSQLId;
	}

	public abstract String getLimitString(String sql, int offset, int limit);
}
