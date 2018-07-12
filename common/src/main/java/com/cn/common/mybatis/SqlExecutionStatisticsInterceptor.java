package com.cn.common.mybatis;

import com.aihuishou.common.util.StringUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.Properties;

@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), 
	@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })})
public class SqlExecutionStatisticsInterceptor implements Interceptor {

	private Logger logger = LoggerFactory
			.getLogger(SqlExecutionStatisticsInterceptor.class);

	private static int MAPPED_STATEMENT_INDEX = 0;
    private static int PARAMETER_INDEX = 1;
	
    private int executionTimeThreshold = 100;
    
	public Object intercept(Invocation invocation) throws Throwable {

		Object object = null;

		long beginTime = System.currentTimeMillis();
    	object = invocation.proceed();
		long endTime = System.currentTimeMillis();
		
		if (endTime - beginTime > executionTimeThreshold) {
			logSql(invocation, endTime - beginTime);
		}

		return object;
	}

	private void logSql(Invocation invocation, long timespan) {
		try {
			Object[] queryArgs =  invocation.getArgs();
			
			if (queryArgs != null && queryArgs.length > 1) {
				MappedStatement statement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
				Object parameter = queryArgs[PARAMETER_INDEX];
				String sqlId = statement.getId();
		        BoundSql boundSql = statement.getBoundSql(parameter);

		        Object param = boundSql.getParameterObject();
		        String sql = boundSql.getSql().trim();	
		        logger.info(MessageFormat.format("[SqlMethod: {0}] [SqlTime:{1}] [Sql: {2}] [Param: {3}]", sqlId, String.valueOf(timespan), sql, StringUtil.toJSONString(param)));
			}
		} catch (Throwable t) {
			logger.error("faied to log sql", t);
		}
	}
	
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties arg0) {

	}

	/**
	 * @return the executionTimeThreshold
	 */
	public int getExecutionTimeThreshold() {
		return executionTimeThreshold;
	}

	/**
	 * @param executionTimeThreshold the executionTimeThreshold to set
	 */
	public void setExecutionTimeThreshold(int executionTimeThreshold) {
		this.executionTimeThreshold = executionTimeThreshold;
	}
	
}
