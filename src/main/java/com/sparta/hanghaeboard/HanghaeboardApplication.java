package com.sparta.hanghaeboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing//스프링 부트의 Entry 포인트인 실행 클래스에 @EnableJpaAuditing 어노테이션을 적용하여 JPA Auditing을 활성화 해준다.
@SpringBootApplication //스프링 부트의 가장 기본적인 설정을 선언해 주는 어노테이션. @Configuration, @EnableAutoConfiguration, @ComponentScan 3가지를 하나의 애노테이션으로 합친 것이다.
public class HanghaeboardApplication {
    public static void main(String[] args) {

        SpringApplication.run(HanghaeboardApplication.class, args);
    }
}
