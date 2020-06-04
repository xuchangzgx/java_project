package cpm.wonders.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CommonResponseResult extends ResponseResult{
    Object data;

    public  CommonResponseResult(ResultCode resultCode, Object data){
        super(resultCode);
        this.data = data;
    }
}
