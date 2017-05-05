package com.imooc.lottery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Info implements Serializable {
	private static final long serialVersionUID = -651280039041345021L;
	private double latitude;
	private double longitude;
	private int imgId;	
	private String name;
	private String phone;
	
	public static List<Info> infos=new ArrayList<Info>();
	
	static{
		//经度，纬度，图片，名称，电话
		infos.add(new Info(118.79725,32.064891, R.drawable._001, "侵华日军南京大屠杀北极阁附近遇难同胞纪念碑","18805160226"));
		infos.add(new Info(118.867286,32.065839, R.drawable._002, "孙中山纪念馆","(025)84430116"));
		infos.add(new Info(118.738427,32.078912, R.drawable._003, "渡江胜利纪念馆","025-84649423"));
		infos.add(new Info(118.738427,32.078912, R.drawable._004, "曹雪芹纪念馆","(025)83714973"));
		infos.add(new Info(118.831791,32.045109, R.drawable._005, "南京博物院","(025)84807923"));	
		infos.add(new Info(118.788432,32.071035, R.drawable._006, "李宗仁公馆旧址","暂无"));
		infos.add(new Info(118.801658,32.037334, R.drawable._007, "郑和纪念馆","(025)84419742"));
		infos.add(new Info(118.803562,32.050355, R.drawable._008, "孙中山就任临时大总统宣誓处(暖阁)","18805160226"));		
		infos.add(new Info(118.796939,32.02805, R.drawable._009, "江南贡院历史陈列馆","(025)52236971"));
		infos.add(new Info(118.804033,32.045225, R.drawable._010, "慰安妇主题纪念馆","025-58598353"));		
		infos.add(new Info(118.787168,32.003366, R.drawable._011, "雨花台烈士纪念馆","(025)52411523"));	
		infos.add(new Info(118.873504,32.060469, R.drawable._012, "辛亥革命腊像馆","(025)84437786"));
		infos.add(new Info(118.77205,32.052469, R.drawable._013, "魏源故居","(025)83714973"));
		infos.add(new Info(118.791771,32.026994, R.drawable._014, "太平天国历史博物馆","025-86623024"));		
	}
	
	
	public Info(double longitude, double latitude, int imgId, String name,String phone) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;		
		this.phone=phone;
	}
	
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
