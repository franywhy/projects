package com.kuaiji.controller;

import com.school.controller.AbstractBaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/learningCenter/web")
public class PracticeController extends AbstractBaseController {

	@RequestMapping({"/practice" })
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = createModelAndView(request, response);
        mav.setViewName("learnCenter/practical_training");
        return mav;
    }
}
