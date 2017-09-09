package com.chap04.concert;

import jdk.nashorn.internal.runtime.regexp.joni.constants.Arguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import soundsystem.CompactDisc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mitmo on 2017-08-26.
 */
@Configuration
@EnableAspectJAutoProxy
public class TrackCounterConfig {
    @Bean
    public TrackCounter trackCounter() {
        return new TrackCounter();
    }
}
