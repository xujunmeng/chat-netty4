package com.cn.server.module.player.dao;

import com.cn.server.module.player.dao.entity.Player;
import org.apache.ibatis.annotations.Param;

/**
 * 玩家dao
 * @author -琴兽-
 *
 */
public interface IPlayerMapper {
	
	/**
	 * 获取玩家通过id
	 * @param playerId
	 * @return
	 */
	Player getPlayerById(@Param("playerId") long playerId);
	
	
	/**
	 * 获取玩家通过玩家名
	 * @param playerName
	 * @return
	 */
	Player getPlayerByName(@Param("playerName") String playerName);
	
	
	/**
	 * 创建玩家
	 * @param player
	 * @return
	 */
	Player createPlayer(Player player);

}
