package test

import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner


/**
 * date: 13-3-11 下午6:13
 * @author: wubinjie@ak.cc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/*.xml")
abstract class BaseTest {
    @Before
    public void setUp() {
    }
}