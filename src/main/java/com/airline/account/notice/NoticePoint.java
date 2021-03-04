package com.airline.account.notice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ydc
 * @date 2021/1/27.
 */
@Aspect
@Component
public class NoticePoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource(name = "weChatFare")
    private Notice notice;

    /**
     * 集中日志组件
     * 可增加不同方法的切点，实现通知分类
     */
    @Pointcut("execution(* com.airline.account..MoveLogMapper.*(..))")
    public void notice(){

    }

    @Before("notice()")
    public void beforeAspect(JoinPoint joinPoint){
        Signature method = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        try {
            String content = objectMapper.writeValueAsString(args);
            notice.send(content, method.getDeclaringTypeName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
