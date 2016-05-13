package com.mljr.carfinance.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import com.mljr.carfinance.common.constants.AccessToken;

public class WebUtil {
    /**
     * 需要在APP启动时候初始化这个值
     */
    public static String APP_ROOT_PATH = "";
    public static ThreadLocal<AccessToken> threadLocal = new ThreadLocal<AccessToken>();

    public static void putAccessToken(AccessToken token) {
        threadLocal.set(token);
    }

    public static AccessToken getAccessToken() {
        return threadLocal.get();
    }

    /**
     * 获取request对象
     * @return
     */
    public static HttpServletRequest getRequest() {
        if (RequestContextHolder.getRequestAttributes() == null) {
            return null;
        } else {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
    }

    /**
     * 获取request里面的参数
     * @param name
     * @return
     */
    public static String getParameter(String name) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            return request.getParameter(name);
        } else {
            return null;
        }
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getUid() {
        AccessToken accessToken = getAccessToken();
        if (accessToken != null) {
            return accessToken.getUi();
        } else {
            return 0L;
        }
    }

    /**
     * 获取request里面的参数
     * @param request
     * @param handlerMethod
     * @return
     */
    public static Map<String, Object> getRequestMethodParams(HttpServletRequest request, HandlerMethod handlerMethod) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        List<Map<String, Object>> formParameters = new ArrayList<Map<String, Object>>();
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        if (methodParameters != null && methodParameters.length > 0) {
            for (MethodParameter methodParameter : methodParameters) {
                String paramName = methodParameter.getParameterName();
                Class<?> paramType = methodParameter.getParameterType();
                Object paramValue = null;
                if (!pathVariables.containsKey(paramName) && paramType != HttpServletRequest.class && paramType != HttpSession.class && paramType != Model.class) {

                    if (paramType.isArray()) {
                        List<String> paramList = new ArrayList<String>();
                        String[] params = request.getParameterValues(paramName);
                        if (params != null && params.length > 0) {
                            for (String param : params) {
                                paramList.add(param);
                            }
                        }
                        paramValue = paramList;
                    } else {
                        paramValue = (String) request.getParameter(paramName);
                        if ("password".equals(paramName) || "pwd".equals(paramName) || "passwd".equals(paramName) && paramValue != null && StrUtil.isNoneBlank(paramValue.toString())) {
                            paramValue = StrUtil.repeat("*", paramValue.toString().length());
                        }
                    }

                    Map<String, Object> formParam = new HashMap<String, Object>();
                    formParam.put(paramName, paramValue);
                    formParameters.add(formParam);
                }
            }
        }
        paramMap.put("pathVariables", pathVariables);
        paramMap.put("formParameters", formParameters);
        return paramMap;
    }
}
