package com.ray.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.LinkDto;
import com.ray.domain.entity.Link;
import com.ray.domain.vo.LinkAdminVo;
import com.ray.domain.vo.PageVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-04 15:14:20
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo listLink(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkDto linkDto);

    LinkAdminVo getLinkById(Long id);

    ResponseResult updateLink(LinkDto linkDto);

    ResponseResult dellink(Long id);
}

