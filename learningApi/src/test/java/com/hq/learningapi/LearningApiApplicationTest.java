package com.hq.learningapi;

import com.hq.learningapi.service.CourseOliveService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by longduyuan on 2018/11/1 0001.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LearningApiApplicationTest {

    @Autowired
    private CourseOliveService courseOliveService;

    @Test
    public void contextLoads() {
        boolean hasAuthority = courseOliveService.checkAuthority(1001, Long.parseLong("11181603"));
        System.out.println(hasAuthority);
    }
}
