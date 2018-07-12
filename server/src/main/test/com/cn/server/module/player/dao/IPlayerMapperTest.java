package com.cn.server.module.player.dao;

import com.cn.server.module.player.dao.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author james
 * @date 2018/7/12
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class IPlayerMapperTest {

    @Autowired
    private IPlayerMapper playerMapper;

    @Test
    public void testgetPlayerById() {
        Player playerById = playerMapper.getPlayerById(1);
        System.out.println(playerById);
    }
    
    @Test
    public void testcreatePlayer() {
        Player player = new Player();
        player.setPlayerId(1);
        player.setPlayerName("test-aa");
        player.setPassward("123456");
        player.setLevel(1);
        player.setExp(2);
        int i = playerMapper.createPlayer(player);
        System.out.println(i);

    }

}
