package com.mljr.carfinance.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.collections.CollectionUtils;

/**
 * 校验工具类
 * @author wdmcygah
 *
 */
public class ValidationUtils {
    public static boolean isDeveloperVersion = false;

    static {
        isDeveloperVersion = PropertiesUtil.getBoolean("server", "app.developer.version");
    }

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ValidationResult validateEntity(T obj) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            result.setHasErrors(true);
            List errorList = new ArrayList();
            for (ConstraintViolation<T> cv : set) {
                Map<String, String> errorMsg = new HashMap<String, String>();
                if (isDeveloperVersion) {
                    errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
                    errorList.add(errorMsg);
                } else {
                    errorList.add(cv.getMessage());
                }
            }
            result.setErrorMsg(errorList);
        }
        return result;
    }

    public static <T> ValidationResult validateProperty(T obj, String propertyName) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        if (CollectionUtils.isNotEmpty(set)) {
            result.setHasErrors(true);
            List errorList = new ArrayList();
            for (ConstraintViolation<T> cv : set) {
                Map<String, String> errorMsg = new HashMap<String, String>();
                if (isDeveloperVersion) {
                    errorMsg.put("", cv.getMessage());
                    errorList.add(errorMsg);
                } else {
                    errorList.add(cv.getMessage());
                }
            }
            result.setErrorMsg(errorList);
        }
        return result;
    }

    public static <T> ValidationResult validatePropertys(T obj, List<String> propertyNames) {
        ValidationResult result = new ValidationResult();
        for (String propertyName : propertyNames) {
            Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
            if (CollectionUtils.isNotEmpty(set)) {
                result.setHasErrors(true);
                List errorList = new ArrayList();
                for (ConstraintViolation<T> cv : set) {
                    Map<String, String> errorMsg = new HashMap<String, String>();
                    if (isDeveloperVersion) {
                        errorMsg.put("", cv.getMessage());
                        errorList.add(errorMsg);
                    } else {
                        errorList.add(cv.getMessage());
                    }
                }
                result.setErrorMsg(errorList);
            }
        }
        return result;
    }

}
