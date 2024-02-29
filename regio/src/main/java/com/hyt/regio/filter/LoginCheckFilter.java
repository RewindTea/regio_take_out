package com.hyt.regio.filter;

/*
* 登录拦截器，检查用户是否已经登录，没有登录则跳转到登录页面，登录成功才能进行其他页面的访问
* */

import com.alibaba.fastjson.JSON;
import com.hyt.regio.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")//拦截所有路径
public class LoginCheckFilter implements Filter {
    //路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //转型成Http
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取本次请求URI
        String requestURI = request.getRequestURI();
        log.info("拦截到URI：{}", request.getRequestURI());

        String[] unCheckURLs = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
        };
        //判断本次请求是否需要处理
        boolean check = check(unCheckURLs, requestURI);

        //不需要处理则放行，check=true
        if (check) {
            log.info("请求{}不需要处理", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        //需要处理，判断登录状态
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户（id：{}）已登录", request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    public boolean check(String[] unCheckURLs, String requestURI) {
        for (String url: unCheckURLs) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
