package com.hq.answerapi.controller;


import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.hq.answerapi.service.impl.CaptchaServiceImpl;
import com.hq.common.enumeration.TransactionStatus;
import com.hq.common.interfaze.AbstractRestController;
import com.hq.common.prototype.WrappedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author hq
 */
@Controller
@RequestMapping("/api")
public class CaptchaController extends AbstractRestController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Autowired
    private CaptchaServiceImpl captchaService;

    private static final Logger TRACER = LoggerFactory.getLogger(CaptchaController.class);

    @RequestMapping(value = "/captcha-image", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse<byte[]>> getCaptchaImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            String schoolId = this.getSchoolId(request);
            String capText = captchaProducer.createText();
            captchaService.saveCaptchaCode(schoolId,request.getSession().getId(),capText);
            BufferedImage bi = captchaProducer.createImage(capText);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bi, "jpg", out);
            return this.success(out.toByteArray());
        } catch (Throwable t) {
            TRACER.error("\nUnKnown Error", t);
        }
        return this.error(TransactionStatus.INTERNAL_SERVER_ERROR);
    }
}
