package com.elise.userinfocenter.config;



import com.elise.userinfocenter.qrcode.QuickResCodeGenerator;
import com.elise.userinfocenter.qrcode.ZxingRepository;
import com.google.zxing.EncodeHintType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ZxingRepository.class)
public class QRCodeConfig {

    private final ZxingRepository prop;

    public QRCodeConfig(ZxingRepository prop) {
        this.prop = prop;
    }

    @Bean("quickResCodeGenerator")
    public QuickResCodeGenerator getGenerator(){
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET,prop.getCharacterSet());
        hints.put(EncodeHintType.ERROR_CORRECTION,prop.getErrorConnection());
        hints.put(EncodeHintType.MARGIN,prop.getMargin());
        hints.put(EncodeHintType.MAX_SIZE,prop.getMaxSize());
        hints.put(EncodeHintType.MIN_SIZE,prop.getMinSize());

        QuickResCodeGenerator generator = new QuickResCodeGenerator(hints,prop.getWidth(),prop.getHeight());
        return generator;
    }





}


