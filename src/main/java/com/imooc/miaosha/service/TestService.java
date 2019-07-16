package com.imooc.miaosha.service;

import com.imooc.miaosha.dao.TestDao;
import com.imooc.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TestService {

    @Autowired
    private TestDao test;

    public Map<String, Object> findAll(){
        return test.findAllOrders();
    }

    public User selectName(int id){
        return test.select(id);
    }

    //@Transactional(readOnly = false)
    public boolean insert() {
        User user_1 = new User(2,"json");
        test.insertUser(user_1);
        User user_2 = new User(1,"jielun");
        test.insertUser(user_2);

        return true;
    }
}
