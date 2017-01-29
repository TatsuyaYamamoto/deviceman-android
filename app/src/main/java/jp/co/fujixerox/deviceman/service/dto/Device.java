package jp.co.fujixerox.deviceman.service.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by TATSUYA-PC4 on 2016/07/03.
 */

public class Device implements Serializable{
    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String name;
}
