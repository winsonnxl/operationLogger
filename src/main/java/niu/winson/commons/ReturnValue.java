package niu.winson.commons;

import niu.winson.entity.ResultVO;
import niu.winson.enumation.ErrorCode;


public class ReturnValue {
    public static ResultVO SUCCESS(Object data){
        return new ResultVO(ErrorCode.SUCCESS,data);
    }

    public static ResultVO SUCCESS(){
        return new ResultVO(ErrorCode.SUCCESS);
    }

    public static ResultVO FAIL(ErrorCode errorCode){
        return new ResultVO(errorCode);
    }

    public static ResultVO FAIL(ErrorCode errorCode,Object data){
        return new ResultVO(errorCode,data);
    }

    public static ResultVO MESSAGE(String errorCode,String errorMsg,Object data){
        return new ResultVO<>(errorCode,errorMsg,data);
    }
}
