package com.zlq.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlq.mall.common.utils.PageUtils;
import com.zlq.mall.member.entity.MemberLoginLogEntity;

import java.util.Map;

/**
 * 会员登录记录
 *
 * @author zhangliqun
 * @email yuzexi0727@gmail.com
 * @date 2022-11-19 15:22:56
 */
public interface MemberLoginLogService extends IService<MemberLoginLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

