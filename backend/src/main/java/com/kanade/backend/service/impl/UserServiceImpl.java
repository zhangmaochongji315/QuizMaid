package com.kanade.backend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.kanade.backend.exception.BusinessException;
import com.kanade.backend.exception.ErrorCode;
import com.kanade.backend.exception.ThrowUtils;
import com.kanade.backend.mapper.UserMapper;
import com.kanade.backend.model.dto.UserLoginDTO;
import com.kanade.backend.model.dto.UserRegisterDTO;
import com.kanade.backend.model.entity.User;
import com.kanade.backend.model.entity.UserSign;
import com.kanade.backend.model.vo.UserLoginVO;
import com.kanade.backend.service.UserService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import static com.kanade.backend.common.Constant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User register(UserRegisterDTO userRegisterDTO) {
        if (StrUtil.isBlank(userRegisterDTO.getUserPassword())|| StrUtil.isBlank(userRegisterDTO.getUserName())||StrUtil.isBlank(userRegisterDTO.getCheckUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        if (userRegisterDTO.getUserName().length() < 3 || userRegisterDTO.getUserName().length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度必须在 3-20 位之间");
        }

        if (userRegisterDTO.getUserPassword().length() < 6 || userRegisterDTO.getUserPassword().length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度必须在 6-20 位之间");
        }

        if (!userRegisterDTO.getUserPassword().equals(userRegisterDTO.getCheckUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",userRegisterDTO.getUserName());

        long count = this.mapper.selectCountByQuery(queryWrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"username 重复");
        }

        String md5DigestAsHex = DigestUtils.md5DigestAsHex(userRegisterDTO.getUserPassword().getBytes());

        User user = new User();
        user.setUsername(userRegisterDTO.getUserName());
        user.setPassword(md5DigestAsHex);
        user.setNickname(userRegisterDTO.getUserName());
        boolean saved = this.save(user);
        if (saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "db fail to save");
        }
        log.info("user{} is saved in database",user.getUsername());
        return user;
    }

    @Override
    public UserLoginVO getLoginUserVO(User user) {
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user,userLoginVO);

        return userLoginVO;
    }

    @Override
    public UserLoginVO UserLogin(UserLoginDTO userLoginDTO, HttpServletRequest request) {
        ThrowUtils.throwIf(StrUtil.isBlank(userLoginDTO.getUserPassword()) ,ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(StrUtil.isBlank(userLoginDTO.getUsername()),ErrorCode.PARAMS_ERROR);

        String md5DigestAsHex = DigestUtils.md5DigestAsHex(userLoginDTO.getUserPassword().getBytes());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("password",md5DigestAsHex);

        User user = this.mapper.selectOneByQuery(queryWrapper);
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"user is not exist or password is wrong");
        }

        request.getSession().setAttribute(USER_LOGIN_STATE,user);
        return null;
    }

    @Override
    public User getUserLoginInfo(HttpServletRequest request) {
        User current = (User) request.getSession().getAttribute(USER_LOGIN_STATE);

        if (current == null || current.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        return current;
    }

    @Override
    public boolean logout(HttpServletRequest request) {
        User user =(User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"not login");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }
}
