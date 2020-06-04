package cpm.wonders.exception;


import cpm.wonders.model.ResultCode;

/**
 * 自定义异常类型
 * @author Administrator
 * @version 1.0
 **/
public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }
    public ResultCode getResultCode(){
        return resultCode;
    }


}
