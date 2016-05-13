package com.mljr.carfinance.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mljr.carfinance.common.constants.Consts;
import com.mljr.carfinance.common.constants.LogInfo;

/**
 * 日志工具类
 * @Description:
 * @Author:Sine Chen
 * @Date:Mar 24, 2016 3:45:49 PM
 * @Copyright: All Rights Reserved. Copyright(c) 2016
 */
public class Logs {
    private static String LEVEL_DEBUG = "DEBUG";
    private static String LEVEL_INFO = "INFO";
    private static String LEVEL_WARN = "WARN";
    private static String LEVEL_ERROR = "ERROR";
    public static Logger R = LoggerFactory.getLogger("com.mljr.carfinance.crm.op");

    private static Logger LOG_SERVICE = LoggerFactory.getLogger("com.mljr.carfinance.crm.op.service");
    private static Logger LOG_CONTROLLER = LoggerFactory.getLogger("com.mljr.carfinance.crm.op.web.controller");

    private static void log(Logger logger, String level, String act, String msg, int code, Object params, long time, Exception e) {
        try {
            LogInfo logInfo = new LogInfo();
            logInfo.setAct(act);
            logInfo.setMsg(msg);
            logInfo.setCode(code);
            logInfo.setUid(WebUtil.getUid());
            logInfo.setParam(params);
            logInfo.setTime(time);
            if (LEVEL_DEBUG.equalsIgnoreCase(level)) {
                logger.debug(logInfo.toString(), e);
            }
            if (LEVEL_INFO.equalsIgnoreCase(level)) {
                logger.info(logInfo.toString(), e);
            }
            if (LEVEL_WARN.equalsIgnoreCase(level)) {
                logger.warn(logInfo.toString(), e);
            }
            if (LEVEL_ERROR.equalsIgnoreCase(level)) {
                logger.error(logInfo.toString(), e);
            }
        } catch (Exception ex) {
            logger.warn("Fail to log!", ex);
        }
    }

    /**
     * Logger for Services
     * @Description:
     * @Author:Sine Chen
     * @Date:Mar 24, 2016 4:30:48 PM
     * @Copyright: All Rights Reserved. Copyright(c) 2016
     */
    public static class S {
        public static void debug(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_SERVICE, LEVEL_DEBUG, act, msg, code, params, time, e);
        }

        public static void debug(String act, String msg, int code, Object params) {
            debug(act, msg, code, params, 0, null);
        }

        public static void debug(String act, String msg, int code) {
            debug(act, msg, code, null, 0, null);
        }

        public static void debug(String act, String msg) {
            debug(act, msg, Consts.STATUS_SUCCESS, null, 0, null);
        }

        public static void info(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_SERVICE, LEVEL_INFO, act, msg, code, params, time, e);
        }

        public static void info(String act, String msg, int code, Object params) {
            info(act, msg, code, params, 0, null);
        }

        public static void info(String act, String msg, int code) {
            info(act, msg, code, null, 0, null);
        }

        public static void info(String act, String msg) {
            info(act, msg, Consts.STATUS_SUCCESS, null, 0, null);
        }

        public static void warn(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_SERVICE, LEVEL_WARN, act, msg, code, params, time, e);
        }

        public static void warn(String act, String msg, int code, Object params) {
            warn(act, msg, code, params, 0, null);
        }

        public static void warn(String act, String msg, Exception e) {
            warn(act, msg, Consts.STATUS_FAIL, null, 0, null);
        }

        public static void warn(String act, String msg, int code) {
            warn(act, msg, code, null, 0, null);
        }

        public static void warn(String act, String msg) {
            warn(act, msg, Consts.STATUS_FAIL, null, 0, null);
        }

        public static void error(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_SERVICE, LEVEL_ERROR, act, msg, code, params, time, e);
        }

        public static void error(String act, String msg, Object params) {
            error(act, msg, Consts.STATUS_ERROR, params, 0, null);
        }

        public static void error(String act, String msg) {
            error(act, msg, Consts.STATUS_ERROR, null, 0, null);
        }

        public static void error(String act, String msg, Exception e) {
            error(act, msg, Consts.STATUS_SUCCESS, null, 0, e);
        }
    }

    /**
     * Logger for Controller
     * @Description:
     * @Author:Sine Chen
     * @Date:Mar 24, 2016 4:31:05 PM
     * @Copyright: All Rights Reserved. Copyright(c) 2016
     */
    public static class C {
        public static void debug(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_CONTROLLER, LEVEL_DEBUG, act, msg, code, params, time, e);
        }

        public static void debug(String act, String msg, int code, Object params) {
            debug(act, msg, code, params, 0, null);
        }

        public static void debug(String act, String msg, int code) {
            debug(act, msg, code, null, 0, null);
        }

        public static void debug(String act, String msg) {
            debug(act, msg, Consts.STATUS_SUCCESS, null, 0, null);
        }

        public static void info(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_CONTROLLER, LEVEL_INFO, act, msg, code, params, time, e);
        }

        public static void info(String act, String msg, int code, Object params) {
            info(act, msg, code, params, 0, null);
        }

        public static void info(String act, String msg, int code) {
            info(act, msg, code, null, 0, null);
        }

        public static void info(String act, String msg) {
            info(act, msg, Consts.STATUS_SUCCESS, null, 0, null);
        }

        public static void warn(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_CONTROLLER, LEVEL_WARN, act, msg, code, params, time, e);
        }

        public static void warn(String act, String msg, int code, Object params) {
            warn(act, msg, code, params, 0, null);
        }

        public static void warn(String act, String msg, int code) {
            warn(act, msg, code, null, 0, null);
        }

        public static void warn(String act, String msg) {
            warn(act, msg, Consts.STATUS_SUCCESS, null, 0, null);
        }

        public static void error(String act, String msg, int code, Object params, long time, Exception e) {
            log(LOG_CONTROLLER, LEVEL_ERROR, act, msg, code, params, time, e);
        }

        public static void error(String act, String msg, Object params) {
            error(act, msg, Consts.STATUS_ERROR, params, 0, null);
        }

        public static void error(String act, String msg) {
            error(act, msg, Consts.STATUS_ERROR, null, 0, null);
        }

        public static void error(String act, String msg, Object params, Exception e) {
            error(act, msg, Consts.STATUS_SUCCESS, params, 0, e);
        }

        public static void error(String act, String msg, Exception e) {
            error(act, msg, Consts.STATUS_SUCCESS, null, 0, e);
        }
    }
}
