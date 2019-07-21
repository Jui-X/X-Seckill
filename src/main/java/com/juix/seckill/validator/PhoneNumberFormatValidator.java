package com.juix.seckill.validator;

import com.juix.seckill.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @param: none
 * @description:
 * @author: KingJ
 * @create: 2019-07-21 17:17
 **/
public class PhoneNumberFormatValidator implements ConstraintValidator<PhoneNumberFormat, String> {

    private boolean required = false;

    @Override
    public void initialize(PhoneNumberFormat constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return ValidatorUtil.isPhoneNumber(value);
        } else {
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                return ValidatorUtil.isPhoneNumber(value);
            }
        }
    }
}
