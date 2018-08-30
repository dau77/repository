package com.equator.service.muser;

import com.equator.dao.MUserMapper;
import com.equator.model.MUser;
import com.equator.service.BServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class MUserServiceImpl extends BServiceImpl<MUserMapper, MUser> implements MUserService {

}
