package com.elise.userinfocenter.config;

import com.elise.userinfocenter.entity.CaptchaProperties;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CaptchaConfig {

    @Autowired
    private CaptchaProperties prop;

    @Bean(name="captchaProducer")
    public DefaultKaptcha getCaptchaBean(){
        DefaultKaptcha defaultKaptcha=new DefaultKaptcha();
        Properties properties=new Properties();
        properties.setProperty("kaptcha.border", prop.getBorder().equals("true")?"yes":"no");
        properties.setProperty("kaptcha.border.color", prop.getBorderColor());
        properties.setProperty("kaptcha.textproducer.font.color", prop.getTextProducerFondColor());
        properties.setProperty("kaptcha.textproducer.font.names", prop.getTextProducerFontNames());
        properties.setProperty("kaptcha.textproducer.char.length", prop.getTextProducerCharLength());
        properties.setProperty("kaptcha.image.width", prop.getImageWidth());
        properties.setProperty("kaptcha.image.height", prop.getImageHeight());
        properties.setProperty("kaptcha.session.key", prop.getSessionKey());
        Config config=new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }


}
