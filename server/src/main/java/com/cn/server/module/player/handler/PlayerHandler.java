package com.cn.server.module.player.handler;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
import com.cn.common.core.model.Result;
import com.cn.common.core.session.Session;
import com.cn.common.module.ModuleId;
import com.cn.common.module.player.PlayerCmd;
import com.cn.common.module.player.request.LoginRequest;
import com.cn.common.module.player.request.RegisterRequest;
import com.cn.common.module.player.response.PlayerResponse;
/**
 * 玩家模块
 * @author -琴兽-
 *
 */
@SocketModule(module = ModuleId.PLAYER)
public interface PlayerHandler {
	
	
	/**
	 * 创建并登录帐号
	 * @param session
	 * @param data {@link RegisterRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.REGISTER_AND_LOGIN)
	Result<PlayerResponse> registerAndLogin(Session session, byte[] data);
	

	/**
	 * 登录帐号
	 * @param session
	 * @param data {@link LoginRequest}
	 * @return
	 */
	@SocketCommand(cmd = PlayerCmd.LOGIN)
	Result<PlayerResponse> login(Session session, byte[] data);

}
