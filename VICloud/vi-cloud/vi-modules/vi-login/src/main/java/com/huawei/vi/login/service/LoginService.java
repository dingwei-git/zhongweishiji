package com.huawei.vi.login.service;

import com.huawei.vi.entity.po.LoginRecordPO;
import com.jovision.jaws.common.dto.LoginDto;
import com.jovision.jaws.common.dto.ModifyPwdDto;
import com.jovision.jaws.common.dto.RenewalTokenDto;
import com.jovision.jaws.common.util.RestResult;
import com.jovision.jaws.common.vo.ExecuteResult;
import com.jovision.jaws.common.vo.RenewalTokenVo;

import javax.servlet.http.HttpServletResponse;


public interface LoginService {
    RestResult login(LoginDto body);

    /**
     * token续约
     *
     * @param body
     * @return
     */
    RestResult renewalToken(RenewalTokenDto body, HttpServletResponse response);


    RestResult modifyPwd(ModifyPwdDto body,String userId);

    /**
     * 登出
     *
     * @param
     * @return
     */
    RestResult logout(String token,String tiken);

    /**
     * 校验密码
     * */
    RestResult pwdrulecheck(String password);


}
