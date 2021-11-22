package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("cityid")
    public String  weatherId;

    public String date;

    public String week;

    public String update_time;


    public String wea;

    public String wea_img;

    @SerializedName("tem")
    public String maxTem;

    @SerializedName("tem2")
    public String minTem;

    @SerializedName("tem1")
    public String nowTem;

    public String win;

    public String win_speed;

    public String humidity;

    public String visibility;

    public String air_level;

    public String air_tips;

    public String air_pm25;

    public String pressure;

    public String city;

    @Override
    public String toString() {
        return "Weather{" +
                "weatherId='" + weatherId + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", update_time='" + update_time + '\'' +
                ", wea='" + wea + '\'' +
                ", wea_img='" + wea_img + '\'' +
                ", maxTem='" + maxTem + '\'' +
                ", minTem='" + minTem + '\'' +
                ", nowTem='" + nowTem + '\'' +
                ", win='" + win + '\'' +
                ", win_speed='" + win_speed + '\'' +
                ", humidity='" + humidity + '\'' +
                ", visibility='" + visibility + '\'' +
                ", air_level='" + air_level + '\'' +
                ", air_tips='" + air_tips + '\'' +
                '}';
    }
}
//{"cityid":"101191101","date":"2021-11-22","week":"星期一","update_time":"2021-11-22 12:10","city":"常州",
//        "cityEn":"changzhou","country":"中国","countryEn":"China","wea":"多云","wea_img":"yun","tem":"9","tem1":"8","tem2":"1",
//        "win":"微风","win_speed":"2级","win_meter":"5km\/h","humidity":"28%","visibility":"20km","pressure":"1018",
//        "air":"56","air_pm25":"56","air_level":"良","air_tips":"空气好，可以外出活动，除极少数对污染物特别敏感的人群以外，对公众没有危害！",
//        "alarm":{"alarm_type":"","alarm_level":"","alarm_content":""}}