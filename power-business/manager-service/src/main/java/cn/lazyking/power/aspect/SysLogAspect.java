package cn.lazyking.power.aspect;

import cn.hutool.core.util.ArrayUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class SysLogAspect {
    /**
     * 切点表达式
     */
    public static final String POINT_CUT = "execution (* cn.lazyking.power.controller.*.*(..))";

    @Around(value = POINT_CUT)
    public Object logAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取 ip 地址
        String ipAddr = request.getRemoteHost();
        // 请求路径
        String requestPath = request.getRequestURI();
        // 获取请求参数
        Object[] args = joinPoint.getArgs();
        // 获取请求方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        // 获取目标方法上的 ApiOperation 注解
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        String operation = null;
        if (annotation != null) {
            // 获取注解中的方法描述
            operation = annotation.value();
        }

        // 判断是否为文件上传的方法, 如果是则标价为文件上传
        String argStr = "";
        if(ArrayUtil.isNotEmpty(args) && args[0] instanceof MultipartFile) {
            argStr = "MultipartFile";
        } else {
            argStr = ArrayUtil.join(args, ",");
        }


        // 记录执行时间
        long startTime = System.currentTimeMillis();
        try {
            // 执行目标方法
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        long execTime = endTime - startTime;

        log.info(
                "请求地址：{}，请求路径: {}, 请求参数：{}，请求方法：{}，请求描述：{}，请求耗时：{}ms",
                ipAddr, requestPath, argStr, methodName, operation, execTime
        );

        return result;
    }

}
