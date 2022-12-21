package com.zlq.mall.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName:zlq-mall
 * @Package:com.zlq.mall.common.vo
 * @ClassName: PageResult
 * @description:
 * @author: LiQun
 * @CreateDate:2022/11/25 15:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult<T>{

    private List<T> resultList;

    private Integer count;

}
