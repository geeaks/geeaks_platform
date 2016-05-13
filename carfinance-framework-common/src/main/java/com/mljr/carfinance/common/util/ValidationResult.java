package com.mljr.carfinance.common.util;

import java.util.List;

/**
 * 校验结果
 * @author wdmcygah
 *
 */
public class ValidationResult {

    //校验结果是否有错
    private boolean hasErrors;

    //校验错误信息
    private List<Object> errorMsg;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public List<Object> getErrorMsg() {
        return errorMsg;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public void setErrorMsg(List<Object> errorMsg) {
        this.errorMsg = errorMsg;
    }
}