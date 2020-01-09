package swjtu.zkd.toutiao.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;

@Component
@Aspect
public class LogAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* swjtu.zkd.toutiao.controller.IndexController.*(..))")
    public void before(JoinPoint joinPoint) {
        StringBuffer sb = new StringBuffer();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg: ").append(arg.toString());
        }
        LOGGER.info("before method: " + sb.toString());
    }

    @After("execution(* swjtu.zkd.toutiao.controller.IndexController.*(..))")
    public void after(JoinPoint joinPoint) {
        LOGGER.info("after method: ");
    }

}
