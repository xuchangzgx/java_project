package cpm.wonders.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cpm.wonders.entity.Customer;
import cpm.wonders.exception.ExceptionCast;
import cpm.wonders.model.CommonCode;
import cpm.wonders.model.CommonResponseResult;
import cpm.wonders.model.ResponseResult;
import cpm.wonders.model.ResultCode;
import cpm.wonders.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@Api("用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value="根据客户id获得客户信息")
    @GetMapping("/getCustomerById")
    public ResponseResult getCustomerById(
            @ApiParam(name = "id",value = "客户id") @RequestParam Long id
    ){
        Customer customerById = userService.getCustomerById(id);
        return new CommonResponseResult(CommonCode.SUCCESS,customerById);
    }

    @ApiOperation(value="分页获得客户信息")
    @GetMapping("/list/{pageNum}/{pageSize}")
    public ResponseResult list(@PathVariable int pageNum, @PathVariable int pageSize) {
        return new CommonResponseResult(CommonCode.SUCCESS,userService.list(pageNum,pageSize));
    }

    @ApiOperation(value="根据用户名称和地址获得用户列表")
    @PostMapping("/list")
    public ResponseResult get(@RequestParam String custName,@RequestParam String address){
        return new CommonResponseResult(CommonCode.SUCCESS,userService.get(custName,address));
    }

    @GetMapping("/listSample")
    @ApiOperation(value="获得所有用户")
    public ResponseResult listSample(){
        return new CommonResponseResult(CommonCode.SUCCESS,userService.listSample());
    }

    @GetMapping("/saveLinkMan")
    @ApiOperation(value="为用户保存联系人")
    public ResultCode save(@ApiParam(name = "id",value = "客户id") @RequestParam Long id){
        try{
            userService.save(id);
        }catch (Exception e){
            e.printStackTrace();
            ExceptionCast.cast(CommonCode.FAIL);
        }

        return CommonCode.SUCCESS;
    }

}
