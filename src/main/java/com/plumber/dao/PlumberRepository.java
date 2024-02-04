package com.plumber.dao;

import com.plumber.entity.Plumber;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface PlumberRepository {

	APIResponse<Object> plumberProfile(Plumber request, Long id) throws APIException;

	void addSkill(Skill request, Long id)throws APIException;

}
