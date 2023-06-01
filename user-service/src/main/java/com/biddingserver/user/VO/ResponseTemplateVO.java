package com.biddingserver.user.VO;

import com.biddingserver.user.entity.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTemplateVO {

    private CustomUser user;

}
