package jp.co.fujixerox.deviceman.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class UserList implements Serializable{
    @Getter
    @Setter
    private List<User> users;
}
