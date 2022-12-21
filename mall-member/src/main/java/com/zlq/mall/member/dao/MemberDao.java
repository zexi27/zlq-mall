package com.zlq.mall.member.dao;

import com.zlq.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:22:56
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
