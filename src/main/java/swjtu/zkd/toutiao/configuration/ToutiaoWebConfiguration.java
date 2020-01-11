package swjtu.zkd.toutiao.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import swjtu.zkd.toutiao.interceptor.LoginRequiredInterceptor;
import swjtu.zkd.toutiao.interceptor.PassportInterceptor;

import java.util.Arrays;

@Component
public class ToutiaoWebConfiguration implements WebMvcConfigurer {

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Autowired
    private PassportInterceptor passportInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor).excludePathPatterns(Arrays.asList("/", "/login", "/register"));
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
