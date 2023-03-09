package com.ray.utils;

import com.ray.domain.entity.Article;
import com.ray.domain.vo.HotArticleVo;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils(){
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //确定返回值类型的泛型
        V result = null;
        try {
            //利用反射创建目标对象
            result = clazz.newInstance();
            //实现属性拷贝
            BeanUtils.copyProperties(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
    /**
     * 测试工具类方法
     */
//    public static void main(String[] args) {
//        Article article=new Article();
//        article.setId(1L);
//        article.setTitle("ss");
//        HotArticleVo hotArticleVo=copyBean(article,HotArticleVo.class);
//        System.out.println(hotArticleVo);
//
//    }

}
