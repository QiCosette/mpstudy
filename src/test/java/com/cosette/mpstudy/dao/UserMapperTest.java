package com.cosette.mpstudy.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.cosette.mpstudy.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cosette.qi on 2019/5/29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void select(){
        List<User> list = userMapper.selectList(null);
        Assert.assertEquals(5,list.size());
        list.forEach(System.out::println);
    }

    @Test
    public void instert(){
        User user = new User();
        user.setName("刘明强");
        user.setAge(31);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        int rows = userMapper.insert(user);
        System.out.println(rows);
    }

    @Test
    public void selectById(){
        User user = userMapper.selectById(1088248166370832385L);
        System.out.println(user);
    }

    @Test
    public void selectIds(){
        List<Long> idsList = Arrays.asList(1088248166370832385L,
                1087982257332887553L);
        List<User> userList = userMapper.selectBatchIds(idsList);
        userList.forEach(System.out::println);
    }

    @Test
    public void selectByMap(){
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name","王天风");
        columnMap.put("age","25");
        List<User> userList = userMapper.selectByMap(columnMap);
        userList.forEach(System.out::println);
    }

    /**
     * 名字中包含雨且年龄小于40
     */
    @Test
    public void selectByWrapper1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","雨").lt("age",40);

        List<User> userList = userMapper.selectList(queryWrapper);

        userList.forEach(System.out::println);
    }

    /**
     * 名字中包含雨且年龄大于等于20且小于等于40且eamil不为空
     * name like '%雨%' and age between 20 and 40 and email is not null
     */
    @Test
    public void selectByWrapper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","雨").between("age",20,40).isNotNull("email");
        List<User> userList = userMapper.selectList(queryWrapper);

        userList.forEach(System.out::println);
    }


    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     * name like '王%' or age>=40 order by age desc, id asc
     */
    @Test
    public void selectByWrapper3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","王").or().ge("age",25)
                .orderByDesc("age").orderByAsc("id");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);

    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d') and manager_id
     * in (select id from user where name like '王%')
     *
     * 此查询有问题
     */
    @Test
    public void selectByWrapper4(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d)={0}","2019-02-14")
                .inSql("manager_id","select id from user where name like '王%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);

    }

    /**
     * 名字为王姓且(年龄小于40或邮箱不为空)
     * name like '王%' and (age<40 or email is not null)
     *
     */
    @Test
    public void selectByWrapper5(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name","王").and(wq -> wq.lt("age",40)
                .or().isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);

    }


    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     * name like '王%' or (age<40 and age>20 and eamil is not null)
     */
    @Test
    public void selectByWrapper6(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name","王")
                .or(wq -> wq.lt("age",40).gt("age",20)
                .isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);

    }


    /**
     * （年龄小于40或邮箱不为空）并且名字为王姓
     * (age<40 and email is not null) and name like '王%'
     */
    @Test
    public void setUserMapper7(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.nested(wq->wq.lt("age",40).or().isNotNull("email"))
                .likeRight("name","王");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 年龄为30、31、34、35
     * age in (30、31、34、35)
     */
    @Test
    public void setUserMapper8(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35));

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 只返回满足条件的其中一条语句即可
     * limit 1
     */
    @Test
    public void selectByWrapper9(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35)).last("limit 1");

        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 名字中包含雨且年龄小于40
     */
    @Test
    public void selectByWrapperSuper1(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("id","name").like("name","雨").lt("age",40);

        List<User> userList = userMapper.selectList(queryWrapper);

        userList.forEach(System.out::println);
    }


    /**
     * 名字中包含雨且年龄小于40
     */
    @Test
    public void selectByWrapperSuper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","雨").lt("age",40)
        .select(User.class,info->!info.getColumn().equals("create_time")&&
        !info.getColumn().equals("manager_id"));

        List<User> userList = userMapper.selectList(queryWrapper);

        userList.forEach(System.out::println);
    }

    @Test
    public void conditionTest(){
        String name = "";
        String email = "x";
        condition(name,email);
    }

    private void condition(String name, String email){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
//        if(StringUtils.isNotEmpty(name)){
//            queryWrapper.like("name",name);
//        }if(StringUtils.isNotEmpty(email)){
//            queryWrapper.like("email",email);
//        }
        queryWrapper.like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email),"email",email);

        List<User> userList = userMapper.selectList(queryWrapper);

        userList.forEach(System.out::println);
    }
}