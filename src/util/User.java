package util;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private boolean online;
	private String pic;		//用户头像的保存路径S
	
	public User () {
		
	}
	
	public User (String name) {
		this.name = name;
	}
	 public String getName() {
	        return name;
	 }

	 public void setName(String name) {
	        this.name = name;
	 }

	 public String getPicture() {
	        return pic;
	 }

	 public void setPicture(String picture) {
	        this.pic = picture;
	 }

	 public boolean getStatus() {
	        return online;
	 }

	 public void setStatus(Boolean status) {
	        this.online = status;
	 }
	
}
