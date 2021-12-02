package com.melon.activity.pojo.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityUpload extends Activity {

    private String locationDesc;

    private String locationPoint;

    private String locationPrecision;

    private String locationId;

}
