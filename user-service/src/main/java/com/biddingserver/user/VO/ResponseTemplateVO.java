package com.liquorstore.user.VO;

import com.liquorstore.user.entity.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO {

    private CustomUser user;
    private Department department;

}
