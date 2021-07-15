package com.weljak.splitter

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class SplitterConfig {
    @Bean
    fun defaultClock():Clock {
        return Clock.systemDefaultZone()
    }
}