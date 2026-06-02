package com.xrtech.web.interceptor;

import com.xrtech.web.context.UserContext;
import com.xrtech.web.context.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户上下文Interceptor
 * 从请求头中提取用户信息，设置到ThreadLocal中供后续使用
 */
@Component
public class UserContextInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserContextInterceptor.class);
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        // 只处理Controller方法，忽略静态资源和其他处理器
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 获取当前调用的Controller方法信息
        String controllerName = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();

        log.debug("进入方法: {}.{}", controllerName, methodName);

        // 从请求头中提取用户信息
        String userId = request.getHeader(USER_ID_HEADER);
        String userRole = request.getHeader(USER_ROLE_HEADER);

        if (userId != null) {
            UserContext userContext = new UserContext(userId, userRole);
            UserContextHolder.setContext(userContext);

            log.debug("用户上下文已设置: userId={}, role={}", userId, userRole);
        }

        // 返回true继续处理，返回false中断请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                          HttpServletResponse response,
                          Object handler,
                          org.springframework.web.servlet.ModelAndView modelAndView)
            throws Exception {

        // Controller方法执行完毕后、视图渲染前调用
        // 可以在这里修改ModelAndView添加公共数据
        if (modelAndView != null) {
            modelAndView.addObject("appName", "XR-Tech API");
            modelAndView.addObject("version", "1.0.0");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                               HttpServletResponse response,
                               Object handler,
                               Exception ex) throws Exception {

        // 视图渲染完成后调用，无论成功还是失败
        // 清理ThreadLocal，防止内存泄漏
        UserContextHolder.clear();

        if (ex != null) {
            log.error("请求处理异常: {}", ex.getMessage(), ex);
        } else {
            log.debug("请求处理完成: {} {}", request.getMethod(), request.getRequestURI());
        }
    }
}