package com.ck.miaosha.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功用这个
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }


    /**
     * 失败用这个
     * @param cm
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }


    public Result(T data){
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    public Result(CodeMsg cm){
        if(cm == null){
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }

}
