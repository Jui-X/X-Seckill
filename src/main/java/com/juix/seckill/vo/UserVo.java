package com.juix.seckill.vo;

import com.juix.seckill.validator.PhoneNumberFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 17:13
 **/
@Data
public class UserVo {

    @NotNull
    @PhoneNumberFormat
    private String phoneNumber;

    @NotNull
    @Length(min = 8)
    private String password;
}
