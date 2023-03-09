package com.ray.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.constants.SystemConstants;
import com.ray.domain.ResponseResult;
import com.ray.domain.dto.LinkDto;
import com.ray.domain.entity.Link;
import com.ray.domain.vo.LinkAdminVo;
import com.ray.domain.vo.LinkVo;
import com.ray.domain.vo.PageVo;
import com.ray.enums.AppHttpCodeEnum;
import com.ray.exception.SystemException;
import com.ray.mapper.LinkMapper;
import com.ray.service.LinkService;
import com.ray.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-02-04 15:14:20
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的链接
        LambdaQueryWrapper<Link> lambdaQueryWrapper=new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Link::getStatus,SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(lambdaQueryWrapper);
        //封装
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public PageVo listLink(Integer pageNum, Integer pageSize, String name, String status) {
        //模糊查询
        LambdaQueryWrapper<Link> lambdaQueryWrapper =
                new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        lambdaQueryWrapper.like(StringUtils.hasText(status),Link::getStatus,status);
        //分页
        Page<Link> page =new Page<>(pageNum,pageSize);
        Page<Link> linkPage = page(page, lambdaQueryWrapper);
        //封装
        List<Link> linkList = linkPage.getRecords();
        List<LinkAdminVo> linkAdminVos = linkList.stream()
                .map(link -> BeanCopyUtils.copyBean(link, LinkAdminVo.class))
                .collect(Collectors.toList());

        return new PageVo(linkAdminVos,linkPage.getTotal());
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        //判空
        if (Objects.isNull(linkDto)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //插入
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        boolean save = save(link);
        //返回
       if (save){
           return ResponseResult.okResult();
       }else {
           return ResponseResult.errorResult(500,"插入操作错误");
       }

    }

    @Override
    public LinkAdminVo getLinkById(Long id) {
        //根据id查询
        Link link = getById(id);
        //封装返回
        return  BeanCopyUtils.copyBean(link,LinkAdminVo.class);
    }

    @Override
    public ResponseResult updateLink(LinkDto linkDto) {
        //判空
        if (Objects.isNull(linkDto)){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        //封装插入
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        boolean update = updateById(link);
        //返回
       if (update){
           return ResponseResult.okResult();
       }else {
           return ResponseResult.errorResult(500,"更新操作失败");
       }


    }

    @Override
    public ResponseResult dellink(Long id) {
        //逻辑删除
        UpdateWrapper<Link> updateWrapper =
                new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("del_flag",1);
        boolean update = update(updateWrapper);
        if (update){
            return ResponseResult.okResult();
        }else {
            return ResponseResult.errorResult(500,"删除操作失败");
        }

    }
}

