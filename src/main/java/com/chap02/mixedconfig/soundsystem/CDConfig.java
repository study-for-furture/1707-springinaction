package com.chap02.mixedconfig.soundsystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soundsystem.CompactDisc;
import soundsystem.SgtPeppers;

/**
 * Created by mitmo on 2017-08-05.
 */
@Configuration
public class CDConfig {
    @Bean
    public CompactDisc compactDisc(){
        return new SgtPeppers();
    }
}
