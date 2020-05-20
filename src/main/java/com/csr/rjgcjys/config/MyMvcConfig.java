package com.csr.rjgcjys.config;

import com.csr.rjgcjys.component.LoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class MyMvcConfig extends WebMvcConfigurerAdapter {
    /**
     * 在配置文件中配置的文件保存路径
     */
    @Value("${cbs.wordsPath}")
    private String mwordsPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(mwordsPath.equals("") || mwordsPath.equals("${cbs.wordsPath}")){
            String wordsPath = MyMvcConfig.class.getClassLoader().getResource("").getPath();
            System.out.print("1.上传配置类wordsPath=="+wordsPath+"\n");
            if(wordsPath.indexOf(".jar")>0){
                wordsPath = wordsPath.substring(0, wordsPath.indexOf(".jar"));
            }else if(wordsPath.indexOf("classes")>0){
                wordsPath = "file:"+wordsPath.substring(0, wordsPath.indexOf("classes"));
            }
            wordsPath = wordsPath.substring(0, wordsPath.lastIndexOf("/"))+"/words/";
            mwordsPath = wordsPath;
        }
        System.out.print("wordsPath============="+mwordsPath+"\n");
        //LoggerFactory.getLogger(WebAppConfig.class).info("imagesPath============="+mImagesPath+"\n");
        registry.addResourceHandler("/words/**").addResourceLocations(mwordsPath);
        // TODO Auto-generated method stub
        System.out.print("2.上传配置类wordsPath=="+mwordsPath+"\n");
        super.addResourceHandlers(registry);
    }


    //所有的WebMvcConfigurerAdapter组件都会一起起作用
    @Bean //将组件注册在容器
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter() {
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("common/login");
            }
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                //super.addInterceptors(registry);
                //静态资源；  *.css , *.js
                //SpringBoot已经做好了静态资源映射
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/","/login/findUserByIdentity","/asserts/**","/webjars/bootstrap/**","/static/**");
            }
        };
        return adapter;
    }
}